public class netty_0007 {

        @Override
        protected void encode(ChannelHandlerContext ctx, ByteBuf uncompressed, ByteBuf out) throws Exception {
            if (finished) {
                out.writeBytes(uncompressed);
                return;
            }
    
            int len = uncompressed.readableBytes();
            if (len == 0) {
                return;
            }
    
            if (uncompressed.hasArray()) {
                // if it is backed by an array we not need to do a copy at all
                encodeSome(uncompressed, out);
            } else {
                int heapBufferSize = Math.min(len, MAX_INPUT_BUFFER_SIZE);
                ByteBuf heapBuf = ctx.alloc().heapBuffer(heapBufferSize, heapBufferSize);
                try {
                    while (uncompressed.isReadable()) {
                        uncompressed.readBytes(heapBuf, Math.min(heapBuf.writableBytes(), uncompressed.readableBytes()));
                        encodeSome(heapBuf, out);
                        heapBuf.clear();
                    }
                } finally {
                    heapBuf.release();
                }
            }
            // clear input so that we don't keep an unnecessary reference to the input array
            deflater.setInput(EmptyArrays.EMPTY_BYTES);
        }
}
