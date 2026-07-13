public class kafka_0274 {

            @Override
            public void process(final Record<K, V> record) {
                            final String DEFAULT_STRING_VALUE = "Skipping record due to null key or value. ";
                // If the key or value is null we don't need to proceed
                if (record.key() == null || record.value() == null) {
                    if (context().recordMetadata().isPresent()) {
                        final RecordMetadata recordMetadata = context().recordMetadata().get();
                        LOG.warn(
                            DEFAULT_STRING_VALUE
                                + "topic=[{}] partition=[{}] offset=[{}]",
                            recordMetadata.topic(), recordMetadata.partition(), recordMetadata.offset()
                        );
                    } else {
                        LOG.warn(
                            "Skipping record due to null key. Topic, partition, and offset not known."
                        );
                    }
                    droppedRecordsSensor.record();
                    return;
                }
    
                final ValueTimestampHeaders<V> oldAggAndTimestamp = store.get(record.key());
                final V oldAgg = getValueOrNull(oldAggAndTimestamp);
    
                final V newAgg;
                final long newTimestamp;
    
                if (oldAgg == null) {
                    newAgg = record.value();
                    newTimestamp = record.timestamp();
                } else {
                    newAgg = reducer.apply(oldAgg, record.value());
                    newTimestamp = Math.max(record.timestamp(), oldAggAndTimestamp.timestamp());
                }
    
                final long putReturnCode = store.put(record.key(), newAgg, newTimestamp, new RecordHeaders());
                // if not put to store, do not forward downstream either
                if (putReturnCode != PUT_RETURN_CODE_NOT_PUT) {
                    tupleForwarder.maybeForward(
                        record.withValue(new Change<>(newAgg, sendOldValues ? oldAgg : null, putReturnCode == PUT_RETURN_CODE_IS_LATEST))
                            .withTimestamp(newTimestamp));
                }
            }
}
