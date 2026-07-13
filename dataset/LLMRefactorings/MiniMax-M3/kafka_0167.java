public class kafka_0167 {

    @Override
    public void process(final Record<KIn, Change<VIn>> record) {
        // the keys should never be null
        if (record.key() == null) {
            throw new StreamsException("Record key for KTable aggregate operator with state " + storeName + " should not be null.");
        }

        final ValueTimestampHeaders<VAgg> oldAggAndTimestamp = store.get(record.key());
        final VAgg oldAgg = getValueOrNull(oldAggAndTimestamp);
        final long[] timestampHolder = { record.timestamp() };
        final VAgg newAgg = computeNewAgg(record, oldAgg, oldAggAndTimestamp, timestampHolder);
        final long newTimestamp = timestampHolder[0];

        // update the store with the new value
        final long putReturnCode = store.put(record.key(), newAgg, newTimestamp, new RecordHeaders());
        // if not put to store, do not forward downstream either
        if (putReturnCode != PUT_RETURN_CODE_NOT_PUT) {
            tupleForwarder.maybeForward(
                record.withValue(new Change<>(newAgg, sendOldValues ? oldAgg : null, putReturnCode == PUT_RETURN_CODE_IS_LATEST))
                    .withTimestamp(newTimestamp));
        }
    }

    private VAgg computeNewAgg(final Record<KIn, Change<VIn>> record,
                               final VAgg oldAgg,
                               final ValueTimestampHeaders<VAgg> oldAggAndTimestamp,
                               final long[] newTimestamp) {
        // first try to remove the old value
        final VAgg intermediateAgg;
        if (record.value().oldValue != null && oldAgg != null) {
            intermediateAgg = remove.apply(record.key(), record.value().oldValue, oldAgg);
            newTimestamp[0] = Math.max(record.timestamp(), oldAggAndTimestamp.timestamp());
        } else {
            intermediateAgg = oldAgg;
        }

        // then try to add the new value
        final VAgg newAgg;
        if (record.value().newValue != null) {
            final VAgg initializedAgg;
            if (intermediateAgg == null) {
                initializedAgg = initializer.apply();
            } else {
                initializedAgg = intermediateAgg;
            }

            newAgg = add.apply(record.key(), record.value().newValue, initializedAgg);
            if (oldAggAndTimestamp != null) {
                newTimestamp[0] = Math.max(record.timestamp(), oldAggAndTimestamp.timestamp());
            }
        } else {
            newAgg = intermediateAgg;
        }
        return newAgg;
    }
}
