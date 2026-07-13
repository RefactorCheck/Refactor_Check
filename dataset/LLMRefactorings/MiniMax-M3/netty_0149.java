public class netty_0149 {

        @Override
        public void channelRead0(ChannelHandlerContext ctx, HttpObject msg) {
            if (msg instanceof HttpRequest) {
                HttpRequest req = (HttpRequest) msg;

                boolean keepAlive = HttpUtil.isKeepAlive(req);
                FullHttpResponse response = buildResponse(ctx, req, keepAlive);

                ChannelFuture f = ctx.write(response);

                if (!keepAlive) {
                    f.addListener(ChannelFutureListener.CLOSE);
                }
            }
        }

        private FullHttpResponse buildResponse(ChannelHandlerContext ctx, HttpRequest req, boolean keepAlive) {
            String hello = HttpHelloWorldServer.content(ctx);

            FullHttpResponse response = new DefaultFullHttpResponse(
                    req.protocolVersion(), OK,
                    Unpooled.copiedBuffer(hello, StandardCharsets.UTF_8));
            response.headers()
                    .set(CONTENT_TYPE, TEXT_PLAIN)
                    .setInt(CONTENT_LENGTH, response.content().readableBytes());

            if (keepAlive) {
                if (!req.protocolVersion().isKeepAliveDefault()) {
                    response.headers().set(CONNECTION, KEEP_ALIVE);
                }
            } else {
                // Tell the client we're going to close the connection.
                response.headers().set(CONNECTION, CLOSE);
            }

            return response;
        }
}
