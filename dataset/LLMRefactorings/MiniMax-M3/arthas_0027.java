public class arthas_0027 {

        private McpSchema.CallToolResult parseToolResult(String resultJson) {
            try {
                Map<String, Object> resultMap = JsonParser.fromJson(resultJson, Map.class);
    
                if (resultMap.containsKey("content")) {
                    return JsonParser.fromJson(resultJson, McpSchema.CallToolResult.class);
                }

                return createTextContentResult(resultJson);
                
            } catch (Exception e) {
                logger.debug("Failed to parse tool result as JSON, treating as plain text", e);
                return createTextContentResult(resultJson);
            }
        }

        private McpSchema.CallToolResult createTextContentResult(String resultJson) {
            McpSchema.TextContent textContent = new McpSchema.TextContent(resultJson);
            return new McpSchema.CallToolResult(
                java.util.Collections.singletonList(textContent),
                false,
                null
            );
        }
}
