public class kafka_0132 {

        @Override
        public void put(final K key,
                        final ValueTimestampHeaders<V> value) {
            Objects.requireNonNull(key, "key cannot be null");
            try {
                maybeMeasureLatency(
                    () -> {
                        if (value == null) {
                            final ProcessorRecordContext currentContext = internalContext.recordContext();
    
                            // Create new headers object to isolate tombstone operation from input record
                            final Headers tombstoneHeaders = new RecordHeaders(currentContext.headers());
    
                            // Create temporary context with new headers
                            final ProcessorRecordContext temporaryContext = new ProcessorRecordContext(
                                currentContext.timestamp(),
                                currentContext.offset(),
                                currentContext.partition(),
                                currentContext.topic(),
                                tombstoneHeaders
                            );
    
                            try {
                                internalContext.setRecordContext(temporaryContext);
                                wrapped().put(serializeKey(key, tombstoneHeaders), serializeValue(null));
                            } finally {
                                // Restore original context
                                internalContext.setRecordContext(currentContext);
                            }
                        } else {
                            // it's ok to only pass headers into `serializeKey`, because for the value case passed-in headers are
                            // getting ignored anyway, because the value (of type `ValueTimestampHeaders`) itself carries the headers
                            final Headers headers = value.headers();
                            wrapped().put(serializeKey(key, headers), serializeValue(value));
                        }
                    },
                    time,
                    putSensor
                );
                maybeRecordE2ELatency();
            } catch (final ProcessorStateException e) {
                final String message = String.format(e.getMessage(), key, value);
                throw new ProcessorStateException(message, e);
            }
        }
}
