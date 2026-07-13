public class kafka_0167 {

            @Override
            public void process(final Record<KIn, Change<VIn>> record) {
                // the keys should never be null
                if (record.key() == null) {
                    throw new StreamsException("Record key for KTable aggregate operator with state " + storeName + " should not be null.");
                }
    
                final ValueTimestampHeaders<VAgg> oldAggAndTimestamp = store.get(record.key());
                final VAgg oldAgg = getValueOrNull(oldAggAndTimestamp);
                final VAgg intermediateAgg;
                long newTimestamp = record.timestamp();
    
                // first try to remove the old value
                if (record.value().oldValue != null && oldAgg != null) {
                    intermediateAgg = remove.apply(record.key(), record.value().oldValue, oldAgg);
                    newTimestamp = Math.max(record.timestamp(), oldAggAndTimestamp.timestamp());
                } else {
                    intermediateAgg = oldAgg;
                }
    
                // then try to add the new value
                final VAgg newAgg;
                if (record.value().newValue != null) {
                    newAgg = add.apply(record.key(), record.value().newValue,
                        intermediateAgg == null ? initializer.apply() : intermediateAgg);
                    if (oldAggAndTimestamp != null) {
                        newTimestamp = Math.max(record.timestamp(), oldAggAndTimestamp.timestamp());
                    }
                } else {
                    newAgg = intermediateAgg;
                }
    
                // update the store with the new value
                final long putReturnCode = store.put(record.key(), newAgg, newTimestamp, new RecordHeaders());
                // if not put to store, do not forward downstream either
                if (putReturnCode != PUT_RETURN_CODE_NOT_PUT) {
                    tupleForwarder.maybeForward(
                        record.withValue(new Change<>(newAgg, sendOldValues ? oldAgg : null, putReturnCode == PUT_RETURN_CODE_IS_LATEST))
                            .withTimestamp(newTimestamp));
                }
            }
}
