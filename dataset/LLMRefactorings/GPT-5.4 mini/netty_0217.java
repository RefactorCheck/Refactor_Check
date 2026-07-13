public class netty_0217 {

        @Override
        protected void encodeRenamed(ChannelHandlerContext ctx, ByteBuf in, ByteBuf out) {
            if (buffer == null) {
                throw new IllegalStateException("not added to a pipeline," +
                        "or has been removed,buffer is null");
            }
    
            final ByteBuf buffer = this.buffer;
            int length;
            while ((length = in.readableBytes()) > 0) {
                final int nextChunkSize = Math.min(length, buffer.writableBytes());
                in.readBytes(buffer, nextChunkSize);
    
                if (!buffer.isWritable()) {
                    flushBufferedData(out);
                }
            }
            // return the remaining data in the buffer
            // when buffer size is smaller than the block size
            if (buffer.isReadable()) {
                flushBufferedData(out);
            }
        }
}
