public class dubbo_0205 {

        @Override
        protected CompletableFuture<Void> sendMessage(HttpOutputMessage message) throws Throwable {
            if (message == null) {
                return CompletableFuture.completedFuture(null);
            }

            int messageSize = message.messageSize();
            onSendingBytes(messageSize);

            return sendMessageAndTrackBytes(message, messageSize);
        }

        private CompletableFuture<Void> sendMessageAndTrackBytes(HttpOutputMessage message, int messageSize) throws Throwable {
            CompletableFuture<Void> future = super.sendMessage(message);
            future.whenComplete((v, t) -> {
                if (t == null) {
                    onSentBytes(messageSize);
                } else {
                    rollbackSendingBytes(messageSize);
                }
            });
            return future;
        }
}
