public class arthas_0056 {

        @Tool(
            name = "stop",
            description = "彻底停止 Arthas。注意停止之后不能再调用任何 tool 了。为了确保 MCP client 能收到返回结果，本 tool 会先返回，再延迟执行 stop。"
        )
        public String stop(
                @ToolParam(description = "延迟执行 stop 的毫秒数，默认 1000ms。用于确保 MCP client 收到返回结果。", required = false)
                Integer delayMs,
                ToolContext toolContext) {
            try {
                int shutdownDelayMs = getDefaultValue(delayMs, DEFAULT_SHUTDOWN_DELAY_MS);
    
                scheduleStop(new ToolExecutionContext(toolContext, false), shutdownDelayMs);
    
                return JsonParser.toJson(createCompletedResponse("Stop scheduled", createStopResult(shutdownDelayMs)));
            } catch (Exception e) {
                logger.error("Error scheduling stop", e);
                return JsonParser.toJson(createErrorResponse("Error scheduling stop: " + e.getMessage()));
            }
        }

        private Map<String, Object> createStopResult(int shutdownDelayMs) {
            Map<String, Object> result = new HashMap<>();
            result.put("command", "stop");
            result.put("scheduled", true);
            result.put("delayMs", shutdownDelayMs);
            result.put("note", "Arthas 将在返回结果后停止，MCP 连接会断开。");
            return result;
        }
}
