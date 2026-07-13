public class netty_0169 {

            final void shutdownInput(boolean allDataRead) {
                logger.trace("shutdownInput Fd: {}", fd().intValue());
                if (!socket.isInputShutdown()) {
                    if (isAllowHalfClosure(config())) {
                        try {
                            socket.shutdown(true, false);
                        } catch (IOException ignored) {
                            // We attempted to shutdown and failed, which means the input has already effectively been
                            // shutdown.
                            fireEventAndClose(ChannelInputShutdownEvent.INSTANCE);
                            return;
                        } catch (NotYetConnectedException ignore) {
                            // We attempted to shutdown and failed, which means the input has already effectively been
                            // shutdown.
                        }
                        pipeline().fireUserEventTriggered(ChannelInputShutdownEvent.INSTANCE);
                    } else {
                        // Handle this same way as if we did read all data so we don't schedule another read.
                        inputClosedSeenErrorOnRead = true;
                        close(voidPromise());
                        return;
                    }
                }
                if (allDataRead && !inputClosedSeenErrorOnRead) {
                    inputClosedSeenErrorOnRead = true;
                    pipeline().fireUserEventTriggered(ChannelInputShutdownReadComplete.INSTANCE);
                }
            }
}
