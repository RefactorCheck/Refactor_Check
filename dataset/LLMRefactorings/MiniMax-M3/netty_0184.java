public class netty_0184 {

            private void handleReadException(ChannelPipeline pipeline, ByteBuf byteBuf, Throwable cause, boolean close,
                                             KQueueRecvByteAllocatorHandle allocHandle) {
                processByteBuf(pipeline, byteBuf);
                if (!failConnectPromise(cause)) {
                    allocHandle.readComplete();
                    pipeline.fireChannelReadComplete();
                    pipeline.fireExceptionCaught(cause);

                    if (shouldShutdownInput(close, cause)) {
                        shutdownInput(false);
                    }
                }
            }

            private void processByteBuf(ChannelPipeline pipeline, ByteBuf byteBuf) {
                if (byteBuf != null) {
                    if (byteBuf.isReadable()) {
                        readPending = false;
                        pipeline.fireChannelRead(byteBuf);
                    } else {
                        byteBuf.release();
                    }
                }
            }

            private boolean shouldShutdownInput(boolean close, Throwable cause) {
                // If oom will close the read event, release connection.
                // See https://github.com/netty/netty/issues/10434
                return close ||
                        cause instanceof OutOfMemoryError ||
                        cause instanceof LeakPresenceDetector.AllocationProhibitedException ||
                        cause instanceof IOException;
            }
}
