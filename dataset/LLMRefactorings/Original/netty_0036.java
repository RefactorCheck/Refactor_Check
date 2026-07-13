public class netty_0036 {

        private void handleReadException(ChannelPipeline pipeline, ByteBuf byteBuf, Throwable cause, boolean close,
                RecvByteBufAllocator.Handle allocHandle) {
            if (byteBuf != null) {
                if (byteBuf.isReadable()) {
                    readPending = false;
                    pipeline.fireChannelRead(byteBuf);
                } else {
                    byteBuf.release();
                }
            }
            allocHandle.readComplete();
            pipeline.fireChannelReadComplete();
            pipeline.fireExceptionCaught(cause);
    
            // If oom will close the read event, release connection.
            // See https://github.com/netty/netty/issues/10434
            if (close ||
                    cause instanceof OutOfMemoryError ||
                    cause instanceof LeakPresenceDetector.AllocationProhibitedException ||
                    cause instanceof IOException) {
                closeOnRead(pipeline);
            }
        }
}
