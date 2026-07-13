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

        if (shouldCloseOnRead(cause, close)) {
            closeOnRead(pipeline);
        }
    }

    private boolean shouldCloseOnRead(Throwable cause, boolean close) {
        return close ||
                cause instanceof OutOfMemoryError ||
                cause instanceof LeakPresenceDetector.AllocationProhibitedException ||
                cause instanceof IOException;
    }
}
