public class netty_0188 {

        @Override
        protected ByteBuf allocateBuffer(ChannelHandlerContext ctx, ByteBuf msg, boolean preferDirect) throws Exception {
            if (!msg.isReadable()) {
                return Unpooled.EMPTY_BUFFER;
            }
    
            Writer writer = getWriter(ctx);
    
            // If Writer is 'null' then Writer is not open.
            if (writer == null) {
                return Unpooled.EMPTY_BUFFER;
            } else {
                writer.encode(msg, preferDirect);
                return writer.writableBuffer;
            }
        }

        private Writer getWriter(ChannelHandlerContext ctx) {
            if (isSharable) {
                return ctx.channel().attr(ATTR).get();
            } else {
                return this.writer;
            }
        }
}
