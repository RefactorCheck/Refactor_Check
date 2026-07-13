public class kafka_0125 {

        private void updateHead() {
            ConsumerRecord<byte[], byte[]> lastCorruptedRecord = null;

            while (headRecord == null && !fifoQueue.isEmpty()) {
                final ConsumerRecord<byte[], byte[]> raw = fifoQueue.pollFirst();
                final ConsumerRecord<Object, Object> deserialized =
                    recordDeserializer.deserialize(processorContext, raw);

                if (deserialized == null) {
                    // this only happens if the deserializer decides to skip. It has already logged the reason.
                    lastCorruptedRecord = raw;
                    continue;
                }

                if (!updateHeadRecord(raw, deserialized)) {
                    lastCorruptedRecord = raw;
                }
            }

            // if all records in the FIFO queue are corrupted, make the last one the headRecord
            // This record is used to update the offsets. See KAFKA-6502 for more details.
            if (headRecord == null && lastCorruptedRecord != null) {
                headRecord = new CorruptedRecord(lastCorruptedRecord);
            }
        }

        private boolean updateHeadRecord(final ConsumerRecord<byte[], byte[]> raw,
                                         final ConsumerRecord<Object, Object> deserialized) {
            final long timestamp;
            try {
                timestamp = timestampExtractor.extract(deserialized, partitionTime);
            } catch (final StreamsException internalFatalExtractorException) {
                throw internalFatalExtractorException;
            } catch (final Exception fatalUserException) {
                throw new StreamsException(
                        String.format("Fatal user code error in TimestampExtractor callback for record %s.", deserialized),
                        fatalUserException);
            }
            log.trace("Source node {} extracted timestamp {} for record {}", source.name(), timestamp, deserialized);

            // drop message if TS is invalid, i.e., negative
            if (timestamp < 0) {
                log.warn(
                        "Skipping record due to negative extracted timestamp. topic=[{}] partition=[{}] offset=[{}] extractedTimestamp=[{}] extractor=[{}]",
                        deserialized.topic(), deserialized.partition(), deserialized.offset(), timestamp, timestampExtractor.getClass().getCanonicalName()
                );
                droppedRecordsSensor.record();
                return false;
            }
            headRecord = new StampedRecord(deserialized, timestamp, raw.key(), raw.value());
            headRecordSizeInBytes = consumerRecordSizeInBytes(raw);
            return true;
        }
}
