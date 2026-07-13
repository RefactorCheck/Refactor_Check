public class nacos_0089 {

    public static EventProcessResult processEvent(io.agentscope.core.agent.Event event) {
        if (event.isLast()) {
            return null;
        }

        Msg msg = event.getMessage();
        if (msg == null) {
            return null;
        }

        StreamResponseType type = StreamResponseType.CONTENT;
        String content = extractContent(event, msg);

        if (event.getType() == EventType.TOOL_RESULT) {
            type = StreamResponseType.TOOL_CALL;
        } else if (event.getType() == EventType.REASONING && hasOnlyThinkBlock(msg)) {
            type = StreamResponseType.THINKING;
        } else {
            type = StreamResponseType.CONTENT;
        }

        if (content == null || content.isEmpty()) {
            return null;
        }

        return new EventProcessResult(type, content);
    }

    private static String extractContent(io.agentscope.core.agent.Event event, Msg msg) {
        EventType eventType = event.getType();
        if (eventType == EventType.TOOL_RESULT) {
            return getTextContent(msg);
        } else if (eventType == EventType.REASONING && hasOnlyThinkBlock(msg)) {
            return getThinkingContent(msg);
        } else {
            return getTextContent(msg);
        }
    }
}
