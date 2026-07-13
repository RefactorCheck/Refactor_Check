public class arthas_0215 {

        @Override
        public InboundResponseResult processInboundResponse(Object responseResult, Object messageId) {
            RequestResolver resolver = this.requestResolvers.remove(messageId);
            if (resolver != null) {
                if (responseResult instanceof McpSchema.JSONRPCResponse) {
                    McpSchema.JSONRPCResponse response = (McpSchema.JSONRPCResponse) responseResult;
                    if (response.getError() != null) {
                        McpError error = new McpError(response.getError());
                        if (resolver.errorHandler != null) {
                            resolver.errorHandler.accept(error);
                        } else {
                            resolver.responseHandler.accept(error);
                        }
                    } else {
                        resolver.responseHandler.accept(response.getResult());
                    }
                } else if (responseResult instanceof Throwable) {
                    if (resolver.errorHandler != null) {
                        resolver.errorHandler.accept((Throwable) responseResult);
                    } else {
                        resolver.responseHandler.accept(responseResult);
                    }
                } else {
                    resolver.responseHandler.accept(responseResult);
                }
                return new InboundResponseResult(true);
            }
    
            return new InboundResponseResult(false);
        }
}
