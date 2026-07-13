public class netty_0120 {

            protected final void handleWriteError(Throwable t) {
                if (t instanceof IOException && config().isAutoClose()) {
                    closeOnError(t);
                } else {
                    try {
                        shutdownOutput(voidPromise(), t);
                    } catch (Throwable t2) {
                        closeOnError(t);
                    }
                }
            }

            private void closeOnError(Throwable t) {
                initialCloseCause = t;
                close(voidPromise(), t, newClosedChannelException(t, "flush0()"));
            }
}
