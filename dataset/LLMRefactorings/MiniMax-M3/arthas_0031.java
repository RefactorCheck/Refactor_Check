public class arthas_0031 {

        private void handleDeleteRequest(ChannelHandlerContext ctx, FullHttpRequest request) {
            if (this.disallowDelete) {
                sendError(ctx, HttpResponseStatus.METHOD_NOT_ALLOWED, new McpError("DELETE method not allowed"));
                return;
            }
    
            McpTransportContext transportContext = this.contextExtractor.extract(request, new DefaultMcpTransportContext());
    
            String sessionId = request.headers().get(HttpHeaders.MCP_SESSION_ID);
            if (sessionId == null) {
                sendError(ctx, HttpResponseStatus.BAD_REQUEST,
                        new McpError("Session ID required in mcp-session-id header"));
                return;
            }
    
            McpStreamableServerSession session = this.sessions.get(sessionId);
            if (session == null) {
                sendError(ctx, HttpResponseStatus.NOT_FOUND, new McpError("Session not found"));
                return;
            }
    
            try {
                session.delete()
                        .thenRun(() -> {
                            this.sessions.remove(sessionId);
                            sendDeleteSuccessResponse(ctx);
                        })
                        .exceptionally(e -> {
                            logger.error("Failed to delete session {}: {}", sessionId, e.getMessage());
                            sendError(ctx, HttpResponseStatus.INTERNAL_SERVER_ERROR,
                                    new McpError(e.getMessage()));
                            return null;
                        });
            } catch (Exception e) {
                logger.error("Failed to delete session {}: {}", sessionId, e.getMessage());
                sendError(ctx, HttpResponseStatus.INTERNAL_SERVER_ERROR,
                        new McpError("Error deleting session"));
            }
        }

        private void sendDeleteSuccessResponse(ChannelHandlerContext ctx) {
            FullHttpResponse response = new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1,
                    HttpResponseStatus.OK
            );
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, 0);
            ctx.writeAndFlush(response);
        }
}
