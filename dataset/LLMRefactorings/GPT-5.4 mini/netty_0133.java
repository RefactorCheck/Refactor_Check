public class netty_0133 {

        @Setup
        public void setupMini() {
            HttpRequestDecoder httpRequestDecoder = new HttpRequestDecoder(
                    HttpRequestDecoder.DEFAULT_MAX_INITIAL_LINE_LENGTH, HttpRequestDecoder.DEFAULT_MAX_HEADER_SIZE,
                    HttpRequestDecoder.DEFAULT_MAX_CHUNK_SIZE, false);
            ChannelInboundHandlerAdapter inboundHandlerAdapter = new ChannelInboundHandlerAdapter() {
                @Override
                public void channelRead(ChannelHandlerContext ctx, Object o) {
                    // this is saving a slow type check on LastHttpContent vs HttpRequest
                    try {
                        if (o == LastHttpContent.EMPTY_LAST_CONTENT) {
                            writeResponse(ctx);
                        }
                    } finally {
                        ReferenceCountUtil.release(o);
                    }
                }
    
                @Override
                public void channelReadComplete(ChannelHandlerContext ctx) {
                    ctx.flush();
                }
    
                private void writeResponse(ChannelHandlerContext ctx) {
                    ByteBuf buffer = ctx.alloc().buffer();
                    // Build the response object.
                    ByteBufUtil.writeAscii(buffer, "HTTP/1.1 200 OK\r\n");
                    ByteBufUtil.writeAscii(buffer, "Content-Length: 0\r\n\r\n");
                    ctx.write(buffer, ctx.voidPromise());
                }
            };
            nettyChannel = new EmbeddedChannel(httpRequestDecoder, inboundHandlerAdapter);
    
            ByteBuf buffer = Unpooled.buffer();
            ByteBufUtil.writeAscii(buffer, "POST / HTTP/1.1\r\n");
            ByteBufUtil.writeAscii(buffer, "Content-Type: text/plain\r\n");
            ByteBufUtil.writeAscii(buffer, "Transfer-Encoding: chunked\r\n\r\n");
            ByteBufUtil.writeAscii(buffer, Integer.toHexString(43) + "\r\n");
            buffer.writeZero(43);
            buffer.writeShort(CRLF_SHORT);
            ByteBufUtil.writeAscii(buffer, Integer.toHexString(18) +
                    ";extension=kjhkasdhfiushdksjfnskdjfbskdjfbskjdfb\r\n");
            buffer.writeZero(18);
            buffer.writeShort(CRLF_SHORT);
            ByteBufUtil.writeAscii(buffer, Integer.toHexString(29) +
                    ";a=12938746238;b=\"lkjkjhskdfhsdkjh\\\"kjshdflkjhdskjhifuwehwi\";c=lkjdshfkjshdiufh\r\n");
            buffer.writeZero(29);
            buffer.writeShort(CRLF_SHORT);
            ByteBufUtil.writeAscii(buffer, Integer.toHexString(9) +
                    ";A;A;A;A;A;A;A;A;A;A;A;A;A;A;A;A;A;A;A;A;A;A;A;A;A;A;A;A;A;A;A;A;A;A;A;A;A;A;A;A;A;A;A;A;A;A;A;A\r\n");
            buffer.writeZero(9);
            buffer.writeShort(CRLF_SHORT);
            ByteBufUtil.writeAscii(buffer, "0\r\n\r\n"); // Last empty chunk
            POST = Unpooled.unreleasableBuffer(buffer);
            readerIndex = POST.readerIndex();
            writeIndex = POST.writerIndex();
        }
}
