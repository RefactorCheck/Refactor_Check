public class arthas_0152 {

    private static final int LOG_PREVIEW_LENGTH = 200;

    @Override
    public CompletableFuture<Void> sendMessage(McpSchema.JSONRPCMessage message, String messageId) {
        return CompletableFuture.runAsync(() -> {
            if (this.closed.get()) {
                logger.warn("Attempted to send message to closed session: {}", this.sessionId);
                return;
            }

            if (!this.ctx.channel().isActive()) {
                logger.warn("Channel for session {} is not active, message will not be sent", this.sessionId);
                return;
            }
            lock.lock();
            try {
                if (this.closed.get()) {
                    logger.debug("Session {} was closed during message send attempt", this.sessionId);
                    return;
                }

                String jsonText = objectMapper.writeValueAsString(message);
                logger.debug("Sending SSE message to session {}: {}", this.sessionId, truncateForLogging(jsonText));
                sendSseEvent(MESSAGE_EVENT_TYPE, jsonText, messageId != null ? messageId : this.sessionId);
                logger.debug("Message sent to session {} with ID {}", this.sessionId, messageId);
            } catch (Exception e) {
                logger.error("Failed to send message to session {}: {}", this.sessionId, e.getMessage());
                this.ctx.close();
            } finally {
                lock.unlock();
            }
        });
    }

    private String truncateForLogging(String jsonText) {
        return jsonText.length() > LOG_PREVIEW_LENGTH
                ? jsonText.substring(0, LOG_PREVIEW_LENGTH) + "..."
                : jsonText;
    }
}
