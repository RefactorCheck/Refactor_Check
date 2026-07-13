public class netty_0055 {

        private Future<Channel> acquireHealthyFromPoolOrNewShifted(final Promise<Channel> promise) {
            try {
                final Channel ch = pollChannel();
                if (ch == null) {
                    // No Channel left in the pool bootstrap a new Channel
                    Bootstrap bs = bootstrap.clone();
                    bs.attr(POOL_KEY, this);
                    ChannelFuture f = connectChannel(bs);
                    if (f.isDone()) {
                        notifyConnect(f, promise);
                    } else {
                        f.addListener((ChannelFutureListener) future -> notifyConnect(future, promise));
                    }
                } else {
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
            } catch (Throwable cause) {
                promise.tryFailure(cause);
            }
            return promise;
        }
}
