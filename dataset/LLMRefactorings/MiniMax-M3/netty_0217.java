public class netty_0217 {

        @Override
        protected void encode(ChannelHandlerContext ctx, ByteBuf in, ByteBuf out) {
            if (buffer == null) {
                throw new IllegalStateException("not added to a pipeline," +
                        "or has been removed,buffer is null");
            }
    
            final ByteBuf buffer = this.buffer;
            while (in.readableBytes() > 0) {
                writeChunk(in, out, buffer);
            }
            // return the remaining data in the buffer
            // when buffer size is smaller than the block size
            if (buffer.isReadable()) {
                flushBufferedData(out);
            }
        }

        private void writeChunk(ByteBuf in, ByteBuf out, ByteBuf buffer) {
            final int length = in.readableBytes();
            final int nextChunkSize = Math.min(length, buffer.writableBytes());
            in.readBytes(buffer, nextChunkSize);

            if (!buffer.isWritable()) {
                flushBufferedData(out);
            }
        }
}
