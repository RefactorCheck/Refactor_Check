public class springframework_0056 {

    @Override
    public final void registerSubscription(Message<?> message) {
        MessageHeaders headers = message.getHeaders();

        SimpMessageType messageType = SimpMessageHeaderAccessor.getMessageType(headers);
        if (!SimpMessageType.SUBSCRIBE.equals(messageType)) {
            throw new IllegalArgumentException("Expected SUBSCRIBE: " + message);
        }

        String sessionId = getRequiredHeader(headers, message, "sessionId", SimpMessageHeaderAccessor::getSessionId);
        if (sessionId == null) {
            return;
        }

        String subscriptionId = getRequiredHeader(headers, message, "subscriptionId", SimpMessageHeaderAccessor::getSubscriptionId);
        if (subscriptionId == null) {
            return;
        }

        String destination = getRequiredHeader(headers, message, "destination", SimpMessageHeaderAccessor::getDestination);
        if (destination == null) {
            return;
        }

        addSubscriptionInternal(sessionId, subscriptionId, destination, message);
    }

    private String getRequiredHeader(MessageHeaders headers, Message<?> message, String fieldName, Function<MessageHeaders, String> headerAccessor) {
        String value = headerAccessor.apply(headers);
        if (value == null) {
            if (logger.isErrorEnabled()) {
                logger.error("No " + fieldName + " in " + message);
            }
        }
        return value;
    }
}
