public class arthas_0276 {

        public CompletableFuture<Void> responseStream(McpSchema.JSONRPCRequest jsonrpcRequest, 
                McpStreamableServerTransport transport, McpTransportContext transportContext) {
            
            McpStreamableServerSessionStream stream = new McpStreamableServerSessionStream(transport);
            McpRequestHandler<?> requestHandler = this.requestHandlers.get(jsonrpcRequest.getMethod());
            
            if (requestHandler == null) {
                MethodNotFoundError error = getMethodNotFoundError(jsonrpcRequest.getMethod());
                McpSchema.JSONRPCResponse errorResponse = new McpSchema.JSONRPCResponse(McpSchema.JSONRPC_VERSION, 
                        jsonrpcRequest.getId(), null,
                        new McpSchema.JSONRPCResponse.JSONRPCError(McpSchema.ErrorCodes.METHOD_NOT_FOUND,
                                error.getMessage(), error.getData()));
    
                // 存储错误响应
                try {
                    eventStore.storeEvent(this.id, errorResponse);
                } catch (Exception e) {
                    logger.warn("Failed to store error response event: {}", e.getMessage());
                }
    
                return transport.sendMessage(errorResponse, null)
                        .thenCompose(v -> transport.closeGracefully());
            }
            ArthasCommandContext commandContext = createCommandContext(transportContext.get(MCP_AUTH_SUBJECT_KEY));
    
            return requestHandler
                    .handle(new McpNettyServerExchange(this.id, stream, clientCapabilities.get(), 
                            clientInfo.get(), transportContext, taskMessageQueue, taskStore), 
                            commandContext, jsonrpcRequest.getParams())
                    .handle((result, ex) -> {
                        if (ex != null) {
                            Throwable cause = ex;
                            if (cause instanceof java.util.concurrent.CompletionException) {
                                cause = cause.getCause();
                            }
    
                            McpSchema.JSONRPCResponse.JSONRPCError jsonRpcError;
                            if (cause instanceof McpError && ((McpError) cause).getJsonRpcError() != null) {
                                jsonRpcError = ((McpError) cause).getJsonRpcError();
                            } else {
                                jsonRpcError = new McpSchema.JSONRPCResponse.JSONRPCError(McpSchema.ErrorCodes.INTERNAL_ERROR,
                                        cause.getMessage(), McpError.aggregateExceptionMessages(cause));
                            }
    
                            return new McpSchema.JSONRPCResponse(McpSchema.JSONRPC_VERSION, jsonrpcRequest.getId(),
                                    null, jsonRpcError);
                        } else {
                            return new McpSchema.JSONRPCResponse(McpSchema.JSONRPC_VERSION,
                                    jsonrpcRequest.getId(), result, null);
                        }
                    })
                    .thenCompose(response -> transport.sendMessage(response, null))
                    .thenCompose(v -> transport.closeGracefully());
        }
}
