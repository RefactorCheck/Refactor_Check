public class arthas_0070 {

        private ToolContext buildEnhancedToolContext(
                String taskId, 
                CreateTaskContext context, 
                ArthasCommandContext isolatedContext) {
            
            Map<String, Object> contextMap = new HashMap<>();
    
            contextMap.put(CREATE_TASK_CONTEXT, context);
            contextMap.put(TASK_ID, taskId);
    
            contextMap.put(COMMAND_CONTEXT, isolatedContext);
    
            if (context.exchange() != null) {
                contextMap.put(EXCHANGE, context.exchange());
    
                McpTransportContext transportContext = context.exchange().getTransportContext();
                if (transportContext != null) {
                    contextMap.put(MCP_TRANSPORT_CONTEXT, transportContext);
                }
            }
            
            return new ToolContext(contextMap);
        }
}
