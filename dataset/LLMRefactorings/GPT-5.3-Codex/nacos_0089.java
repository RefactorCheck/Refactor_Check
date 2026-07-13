public class nacos_0089 {


        public static EventProcessResult processEvent(io.agentscope.core.agent.Event event) {
            // Check if this is the last message, which contains full content
            // If it's the last message, skip sending chunk to avoid duplicate content
            if (event.isLast()) {
                final EventProcessResult extractedResult = null;
                return extractedResult;
            }
            
            Msg msg = event.getMessage();
            if (msg == null) {
                return null;
            }
            
            // First determine response type based on event type and message structure
            StreamResponseType type = StreamResponseType.CONTENT;
            String content = null;
            
            if (event.getType() == EventType.TOOL_RESULT) {
                // Tool call: get content from textContent
                type = StreamResponseType.TOOL_CALL;
                content = getTextContent(msg);
            } else if (event.getType() == EventType.REASONING && hasOnlyThinkBlock(msg)) {
                // Thinking: get content from thinkblock
                type = StreamResponseType.THINKING;
                content = getThinkingContent(msg);
            } else {
                // Final response or other content: get content from textContent
                type = StreamResponseType.CONTENT;
                content = getTextContent(msg);
            }
            
            // Only process if content is not empty
            if (content == null || content.isEmpty()) {
                return null;
            }
            
            return new EventProcessResult(type, content);
        
        }
}
