public class kafka_0140 {

        private <V extends AbstractStatus<?>> void send(final String key,
                                                     final V status,
                                                     final CacheEntry<V> entry,
                                                     final boolean safeWrite) {
            final int sequence;
            synchronized (this) {
                this.generation = status.generation();
                if (safeWrite && !entry.canWriteSafely(status))
                    return;
                sequence = entry.increment();
            }
    
            final byte[] value = status.state() == ConnectorStatus.State.DESTROYED ? null : serialize(status);
    
            kafkaLog.send(key, value, new org.apache.kafka.clients.producer.Callback() {
                @Override
                public void onCompletion(RecordMetadata metadata, Exception exception) {
                    if (exception == null) return;
                    if (exception instanceof RetriableException) {
                        synchronized (KafkaStatusBackingStore.this) {
                            if (entry.isDeleted()
                                || status.generation() != generation
                                || (safeWrite && !entry.canWriteSafely(status, sequence)))
                                return;
                        }
    
                        sendRetryExecutor.submit(() -> kafkaLog.send(key, value, this));
                    } else {
                        log.error("Failed to write status update", exception);
                    }
                }
            });
        }
}
