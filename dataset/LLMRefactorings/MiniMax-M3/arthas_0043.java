public class arthas_0043 {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        String path = new URI(request.uri()).getPath();
        HttpMethod method = request.method();
        FullHttpResponse resp = null;

        if (HttpMethod.OPTIONS.equals(method)) {
            resp = httpOptionRequestHandler.handleOptionsRequest(ctx, request);
        }

        if (HttpMethod.POST.equals(method)) {
            if ("/api/native-agent".equals(path)) {
                resp = httpNativeAgentHandler.handle(ctx, request);
            }
        }

        if (resp == null) {
            resp = createNotFoundResponse(request);
        }

        ctx.writeAndFlush(resp).addListener(ChannelFutureListener.CLOSE);
    }

    private FullHttpResponse createNotFoundResponse(FullHttpRequest request) {
        FullHttpResponse resp = new DefaultFullHttpResponse(request.getProtocolVersion(), HttpResponseStatus.NOT_FOUND);
        resp.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=utf-8");
        return resp;
    }
}
