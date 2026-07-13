public class dubbo_0196 {

        private void handleDelete(StreamObserver<ServerSentEvent<byte[]>> responseObserver) {
            HttpRequest request = RpcContext.getServiceContext().getRequest(HttpRequest.class);
            HttpResponse response = RpcContext.getServiceContext().getResponse(HttpResponse.class);
    
            String sessionId = request.header(SESSION_ID_HEADER);
            if (StringUtils.isBlank(sessionId)) {
                byte[] errorBody = new McpError("Session ID required in mcp-session-id header").getJsonRpcError();
                response.setStatus(HttpStatus.BAD_REQUEST.getCode());
                response.setBody(errorBody);
                if (responseObserver != null) {
                    responseObserver.onError(HttpResult.builder()
                            .status(HttpStatus.BAD_REQUEST.getCode())
                            .body(errorBody)
                            .build()
                            .toPayload());
                    responseObserver.onCompleted();
                }
                return;
            }
    
            McpStreamableServerSession session = sessions.get(sessionId);
            if (session == null) {
                response.setStatus(HttpStatus.NOT_FOUND.getCode());
                if (responseObserver != null) {
                    responseObserver.onCompleted();
                }
                return;
            }
    
            try {
                session.delete().block();
                sessions.remove(sessionId);
                response.setStatus(HttpStatus.OK.getCode());
                if (responseObserver != null) {
                    responseObserver.onNext(ServerSentEvent.<byte[]>builder()
                            .event("response")
                            .data("{\"status\":\"deleted\"}".getBytes(StandardCharsets.UTF_8))
                            .build());
                    responseObserver.onCompleted();
                }
            } catch (Exception e) {
                byte[] errorBody = new McpError(e.getMessage()).getJsonRpcError();
                response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.getCode());
                response.setBody(errorBody);
                if (responseObserver != null) {
                    responseObserver.onError(HttpResult.builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.getCode())
                            .body(errorBody)
                            .build()
                            .toPayload());
                    responseObserver.onCompleted();
                }
            }
        }
}
