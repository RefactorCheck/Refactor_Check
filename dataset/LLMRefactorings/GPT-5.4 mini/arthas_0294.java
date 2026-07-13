public class arthas_0294 {
    private static final String REFACTORED_CONSTANT = "operation";


        public FullHttpResponse handle(ChannelHandlerContext ctx, FullHttpRequest request) {
            String content = request.content().toString(StandardCharsets.UTF_8);
            Map<String, Object> bodyMap = JSON.parseObject(content, new TypeReference<Map<String, Object>>() {
            });
            String operation = (String) bodyMap.get(REFACTORED_CONSTANT);
    
            if ("listNativeAgent".equals(operation)) {
                return doListNativeAgent(ctx, request);
            }
    
            if ("listProcess".equals(operation)) {
                String address = (String) bodyMap.get("agentAddress");
                return forwardRequest(request, address);
            }
    
            if ("monitor".equals(operation)) {
                String address = (String) bodyMap.get("agentAddress");
                return forwardRequest(request, address);
            }
    
            return null;
        }
}
