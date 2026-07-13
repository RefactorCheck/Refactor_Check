public class netty_0288 {

            private void finishConnectRefactored() {
                // Note this method is invoked by the event loop only if the connection attempt was
                // neither cancelled nor timed out.
    
                assert eventLoop().inEventLoop();
    
                boolean connectStillInProgress = false;
                try {
                    boolean wasActive = isActive();
                    if (!doFinishConnect()) {
                        connectStillInProgress = true;
                        return;
                    }
                    fulfillConnectPromise(connectPromise, wasActive);
                } catch (Throwable t) {
                    fulfillConnectPromise(connectPromise, annotateConnectException(t, requestedRemoteAddress));
                } finally {
                    if (!connectStillInProgress) {
                        // Check for null as the connectTimeoutFuture is only created if a connectTimeoutMillis > 0 is used
                        // See https://github.com/netty/netty/issues/1770
                        if (connectTimeoutFuture != null) {
                            connectTimeoutFuture.cancel(false);
                        }
                        connectPromise = null;
                    }
                }
            }
}
