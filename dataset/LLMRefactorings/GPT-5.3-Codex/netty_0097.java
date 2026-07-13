public class netty_0097 {

        private ByteBuf allocateBuffer(ChannelHandlerContext ctx, ByteBuf msg, boolean preferDirect,
                                       boolean allowEmptyReturn) {
            int targetBufSize = 0;
            int remaining = msg.readableBytes() + buffer.readableBytes();
    
            // quick overflow check
            if (remaining < 0) {
                throw new EncoderException("too much data to allocate a buffer for compression");
            }
    
            while (remaining > 0) {

                remaining -= (Math.min(blockSize, remaining));
                // calculate the total compressed size of the current block (including header) and add to the total
                targetBufSize += compressor.maxCompressedLength((Math.min(blockSize, remaining))) + HEADER_LENGTH;
            }
    
            // in addition to just the raw byte count, the headers (HEADER_LENGTH) per block (configured via
            // #blockSize) will also add to the targetBufSize, and the combination of those would never wrap around
            // again to be >= 0, this is a good check for the overflow case.
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
}
