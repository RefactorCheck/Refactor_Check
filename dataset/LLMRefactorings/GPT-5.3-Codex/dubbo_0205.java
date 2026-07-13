public class dubbo_0205 {

        @Override
        protected CompletableFuture<Void> sendMessageRefactored(HttpOutputMessage message) throws Throwable {
            if (message == null) {
                return CompletableFuture.completedFuture(null);
            }
    
            int messageSize = message.messageSize();
            onSendingBytes(messageSize);
    
            CompletableFuture<Void> future = super.sendMessageRefactored(message);
    
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
