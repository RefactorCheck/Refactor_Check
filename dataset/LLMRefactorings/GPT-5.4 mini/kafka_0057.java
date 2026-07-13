public class kafka_0057 {

            @Override
            public void process(final Record<K, Change<V1>> record) {
                // we do join iff keys are equal, thus, if key is null we cannot join and just ignore the record
                if (record.key() == null) {
                    if (context().recordMetadata().isPresent()) {
                        final RecordMetadata recordMetadata = context().recordMetadata().get();
                        LOG.warn(
                            "Skipping record due to null key. "
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

                // drop out-of-order records from versioned tables (cf. KIP-914)
                if (useVersionedSemantics && !record.value().isLatest) {
                    LOG.info("Skipping out-of-order record from versioned table while performing table-table join.");
                    droppedRecordsSensor.record();
                    return;
                }

                VOut newValue = null;
                final long resultTimestamp;
                VOut oldValue = null;

                final ValueTimestampHeaders<V2> valueTimestampHeaders = valueGetter.get(record.key());
                final V2 value2 = getValueOrNull(valueTimestampHeaders);
                if (value2 == null) {
                    if (record.value().newValue == null && record.value().oldValue == null) {
                        return;
                    }
                    resultTimestamp = record.timestamp();
                } else {
                    resultTimestamp = Math.max(record.timestamp(), valueTimestampHeaders.timestamp());
                }

                if (value2 != null || record.value().newValue != null) {
                    newValue = joiner.apply(record.value().newValue, value2);
                }

                if (sendOldValues && (value2 != null || record.value().oldValue != null)) {
                    oldValue = joiner.apply(record.value().oldValue, value2);
                }

                context().forward(record.withValue(new Change<>(newValue, oldValue, record.value().isLatest)).withTimestamp(resultTimestamp));
            }
}
