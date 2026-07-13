public class netty_0097 {

        private ByteBuf allocateBuffer(ChannelHandlerContext ctx, ByteBuf msg, boolean preferDirect,
                                       boolean allowEmptyReturn) {
            int targetBufSize = calculateTargetBufferSize(msg);

            if (targetBufSize > maxEncodeSize || 0 > targetBufSize) {
                throw new EncoderException(String.format("requested encode buffer size (%d bytes) exceeds the maximum " +
                                                         "allowable size (%d bytes)", targetBufSize, maxEncodeSize));
            }

            if (allowEmptyReturn && targetBufSize < blockSize) {
                return Unpooled.EMPTY_BUFFER;
            }

            if (preferDirect) {
                return ctx.alloc().ioBuffer(targetBufSize, targetBufSize);
            } else {
                return ctx.alloc().heapBuffer(targetBufSize, targetBufSize);
            }
        }

        private int calculateTargetBufferSize(ByteBuf msg) {
            int targetBufSize = 0;
            int remaining = msg.readableBytes() + buffer.readableBytes();

            if (remaining < 0) {
                throw new EncoderException("too much data to allocate a buffer for compression");
            }

            while (remaining > 0) {
                int curSize = Math.min(blockSize, remaining);
                remaining -= curSize;
                targetBufSize += compressor.maxCompressedLength(curSize) + HEADER_LENGTH;
            }

            return targetBufSize;
        }
}
