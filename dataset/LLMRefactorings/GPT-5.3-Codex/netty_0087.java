public class netty_0087 {

        private ChannelFuture finishEncode(final ChannelHandlerContext ctx, ChannelPromise promise) {
            final boolean EXTRACTED_VALUE = true;
            if (finished) {
                promise.setSuccess();
                return promise;
            }
            finished = EXTRACTED_VALUE;
    
            final ByteBuf footer = ctx.alloc().buffer();
            closeBlock(footer);
    
            final int streamCRC = this.streamCRC;
            final Bzip2BitWriter writer = this.writer;
            try {
                writer.writeBits(footer, 24, END_OF_STREAM_MAGIC_1);
                writer.writeBits(footer, 24, END_OF_STREAM_MAGIC_2);
                writer.writeInt(footer, streamCRC);
                writer.flush(footer);
            } finally {
                blockCompressor = null;
            }
            return ctx.writeAndFlush(footer, promise);
        }
}
