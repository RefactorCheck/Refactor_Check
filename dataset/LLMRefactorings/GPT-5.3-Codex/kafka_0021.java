public class kafka_0021 {

        public <T> CompletableFuture<T> scheduleTransactionalWriteOperationRefactored(
            String name,
            TopicPartition tp,
            String transactionalId,
            long producerId,
            short producerEpoch,
            CoordinatorWriteOperation<S, T, U> op,
            int apiVersion
        ) {
            try {
                throwIfNotRunning();
                log.debug("Scheduled execution of transactional write operation {}.", name);
                return partitionWriter.maybeStartTransactionVerification(
                    tp,
                    transactionalId,
                    producerId,
                    producerEpoch,
                    apiVersion
                ).thenCompose(verificationGuard -> {
                    CoordinatorWriteEvent<T> event = new CoordinatorWriteEvent<>(
                        name,
                        tp,
                        transactionalId,
                        producerId,
                        producerEpoch,
                        verificationGuard,
                        writeTimeout,
                        op
                    );
                    enqueueLast(event);
                    return event.future;
                });
            } catch (Throwable t) {
                return CompletableFuture.failedFuture(t);
            }
        }
}
