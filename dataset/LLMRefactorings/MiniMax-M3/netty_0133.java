public class netty_0133 {

    private static final String HTTP_RESPONSE_STATUS = "HTTP/1.1 200 OK\r\n";
    private static final String HTTP_RESPONSE_HEADERS = "Content-Length: 0\r\n\r\n";
    private static final String HTTP_REQUEST_LINE = "POST / HTTP/1.1\r\n";
    private static final String CONTENT_TYPE_HEADER = "Content-Type: text/plain\r\n";
    private static final String TRANSFER_ENCODING_HEADER = "Transfer-Encoding: chunked\r\n\r\n";
    private static final String LAST_CHUNK = "0\r\n\r\n";

    @Setup
    public void setup() {
        HttpRequestDecoder httpRequestDecoder = new HttpRequestDecoder(
                HttpRequestDecoder.DEFAULT_MAX_INITIAL_LINE_LENGTH, HttpRequestDecoder.DEFAULT_MAX_HEADER_SIZE,
                HttpRequestDecoder.DEFAULT_MAX_CHUNK_SIZE, false);
        ChannelInboundHandlerAdapter inboundHandlerAdapter = new ChannelInboundHandlerAdapter() {
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object o) {
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
                ByteBufUtil.writeAscii(buffer, HTTP_RESPONSE_STATUS);
                ByteBufUtil.writeAscii(buffer, HTTP_RESPONSE_HEADERS);
                ctx.write(buffer, ctx.voidPromise());
            }
        };
        nettyChannel = new EmbeddedChannel(httpRequestDecoder, inboundHandlerAdapter);

        ByteBuf buffer = Unpooled.buffer();
        ByteBufUtil.writeAscii(buffer, HTTP_REQUEST_LINE);
        ByteBufUtil.writeAscii(buffer, CONTENT_TYPE_HEADER);
        ByteBufUtil.writeAscii(buffer, TRANSFER_ENCODING_HEADER);
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
        ByteBufUtil.writeAscii(buffer, LAST_CHUNK);
        POST = Unpooled.unreleasableBuffer(buffer);
        readerIndex = POST.readerIndex();
        writeIndex = POST.writerIndex();
    }
}
