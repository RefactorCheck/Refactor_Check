public class arthas_0022 {

        public void handle(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
            String uri = request.uri();
            if (!uri.endsWith(mcpEndpoint)) {
                sendError(ctx, HttpResponseStatus.NOT_FOUND, new McpError("Endpoint not found"));
                return;
            }
    
            if (isClosing.get()) {
                sendError(ctx, HttpResponseStatus.SERVICE_UNAVAILABLE, new McpError("Server is shutting down"));
                return;
            }
    
            logger.debug("Request {} {} -> using {} transport",
                request.method(), request.uri(), protocol);
    
            try {
                dispatchRequest(ctx, request);
            } catch (Exception e) {
                logger.error("Error handling request: {}", e.getMessage(), e);
                sendError(ctx, HttpResponseStatus.INTERNAL_SERVER_ERROR,
                    new McpError("Error processing request: " + e.getMessage()));
            }
        }

        private void dispatchRequest(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
            if (protocol == ServerProtocol.STREAMABLE) {
                if (streamableHandler == null) {
                    sendError(ctx, HttpResponseStatus.SERVICE_UNAVAILABLE,
                        new McpError("Streamable transport handler not available"));
                    return;
                }
                streamableHandler.handle(ctx, request);
            } else {
                if (statelessHandler == null) {
                    sendError(ctx, HttpResponseStatus.SERVICE_UNAVAILABLE,
                        new McpError("Stateless transport handler not available"));
                    return;
                }
                statelessHandler.handle(ctx, request);
            }
        }
}
