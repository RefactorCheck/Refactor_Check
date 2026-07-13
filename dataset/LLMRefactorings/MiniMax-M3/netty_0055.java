public class netty_0055 {

        private Future<Channel> acquireHealthyFromPoolOrNew(final Promise<Channel> promise) {
            try {
                final Channel ch = pollChannel();
                if (ch == null) {
                    bootstrapNewChannel(promise);
                } else {
                    scheduleHealthCheck(ch, promise);
                }
            } catch (Throwable cause) {
                promise.tryFailure(cause);
            }
            return promise;
        }

        private void bootstrapNewChannel(Promise<Channel> promise) {
            Bootstrap bs = bootstrap.clone();
            bs.attr(POOL_KEY, this);
            ChannelFuture f = connectChannel(bs);
            if (f.isDone()) {
                notifyConnect(f, promise);
            } else {
                f.addListener((ChannelFutureListener) future -> notifyConnect(future, promise));
            }
        }

        private void scheduleHealthCheck(Channel ch, Promise<Channel> promise) {
            EventLoop loop = ch.eventLoop();
            if (loop.inEventLoop()) {
                doHealthCheck(ch, promise);
            } else {
                loop.execute(new Runnable() {
                    @Override
                    public void run() {
                        doHealthCheck(ch, promise);
                    }
                });
            }
        }
}
