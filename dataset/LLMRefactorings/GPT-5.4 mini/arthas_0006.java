public class arthas_0006 {
    private String uri;


        protected void handle(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
            uri = request.uri();
            if (!uri.endsWith(mcpEndpoint)) {
                sendError(ctx, HttpResponseStatus.NOT_FOUND, new McpError("Endpoint not found"));
                return;
            }
    
            if (isClosing.get()) {
                sendError(ctx, HttpResponseStatus.SERVICE_UNAVAILABLE, new McpError("Server is shutting down"));
                return;
            }
    
            HttpMethod method = request.method();
            if (method == HttpMethod.GET) {
                handleGetRequest(ctx, request);
            } else if (method == HttpMethod.POST) {
                handlePostRequest(ctx, request);
            } else if (method == HttpMethod.DELETE) {
                handleDeleteRequest(ctx, request);
            } else {
                sendError(ctx, HttpResponseStatus.METHOD_NOT_ALLOWED, new McpError("Method not allowed"));
            }
        }
}
