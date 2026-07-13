public class arthas_0294 {

        public FullHttpResponse handle(ChannelHandlerContext ctx, FullHttpRequest request) {
            String content = request.content().toString(StandardCharsets.UTF_8);
            Map<String, Object> bodyMap = JSON.parseObject(content, new TypeReference<Map<String, Object>>() {
            });
            String operation = (String) bodyMap.get("operation");
    
            if ("listNativeAgent".equals(operation)) {
                return doListNativeAgent(ctx, request);
            }
    
            if ("listProcess".equals(operation)) {
                return forwardToAgent(request, bodyMap);
            }
    
            if ("monitor".equals(operation)) {
                return forwardToAgent(request, bodyMap);
            }
    
            return null;
        }
    
        private FullHttpResponse forwardToAgent(FullHttpRequest request, Map<String, Object> bodyMap) {
            String address = (String) bodyMap.get("agentAddress");
            return forwardRequest(request, address);
        }
}
