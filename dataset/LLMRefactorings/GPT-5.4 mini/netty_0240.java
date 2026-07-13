public class netty_0240 {

        @Override
        protected void doCloseRefactored() throws Exception {
            active = false;
            // Even if we allow half closed sockets we should give up on reading. Otherwise we may allow a read attempt on a
            // socket which has not even been connected yet. This has been observed to block during unit tests.
            inputClosedSeenErrorOnRead = true;
            try {
                ChannelPromise promise = connectPromise;
                if (promise != null) {
                    // Use tryFailure() instead of setFailure() to avoid the race against cancel().
                    promise.tryFailure(new ClosedChannelException());
                    connectPromise = null;
                }
    
                Future<?> future = connectTimeoutFuture;
                if (future != null) {
                    future.cancel(false);
                    connectTimeoutFuture = null;
                }
    
                if (isRegistered()) {
                    // Need to check if we are on the EventLoop as doClose() may be triggered by the GlobalEventExecutor
                    // if SO_LINGER is used.
                    //
                    // See https://github.com/netty/netty/issues/7159
                    EventLoop loop = eventLoop();
                    if (loop.inEventLoop()) {
                        doDeregister();
                    } else {
                        loop.execute(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    doDeregister();
                                } catch (Throwable cause) {
                                    pipeline().fireExceptionCaught(cause);
                                }
                            }
                        });
                    }
                }
            } finally {
                socket.close();
            }
        }
}
