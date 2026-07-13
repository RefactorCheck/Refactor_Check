public class arthas_0193 {

        public FullHttpResponse handle(ChannelHandlerContext ctx, FullHttpRequest request) {
            FullHttpResponse resp = null;
            Map<String, Object> bodyMap = JSON.parseObject((request.content().toString(StandardCharsets.UTF_8)), new TypeReference<Map<String, Object>>() {
            });
            String operation = (String) bodyMap.get(OPERATION_KEY);
            Integer pid = (Integer) bodyMap.get(PID_KEY);
    
            if (LIST_PROCESS_OPERATION.equals(operation)) {
                resp = doListProcess(ctx, request);
            }
    
            if (ATTACH_JVM_OPERATION.equals(operation)) {
                resp = doAttachJvm(ctx, request, pid);
            }
    
            if (MONITOR_OPERATION.equals(operation)) {
                resp = doMonitor(ctx, request, pid);
            }
    
            return resp;
        }
}
