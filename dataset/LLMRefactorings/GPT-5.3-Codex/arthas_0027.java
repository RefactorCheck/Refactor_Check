public class arthas_0027 {
        private Map<String, Object> resultMap;


        private McpSchema.CallToolResult parseToolResult(String resultJson) {
            try {
                resultMap = JsonParser.fromJson(resultJson, Map.class);
    
                if (resultMap.containsKey("content")) {
                    return JsonParser.fromJson(resultJson, McpSchema.CallToolResult.class);
                }
    
                McpSchema.TextContent textContent = new McpSchema.TextContent(resultJson);
                return new McpSchema.CallToolResult(
                    java.util.Collections.singletonList(textContent),
                    false,
                    null
                );
                
            } catch (Exception e) {
                logger.debug("Failed to parse tool result as JSON, treating as plain text", e);
                
                McpSchema.TextContent textContent = new McpSchema.TextContent(resultJson);
                return new McpSchema.CallToolResult(
                    java.util.Collections.singletonList(textContent),
                    false,
                    null
                );
            }
        }
}
