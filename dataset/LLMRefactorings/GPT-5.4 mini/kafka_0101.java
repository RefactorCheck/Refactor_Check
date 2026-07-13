public class kafka_0101 {

        <K, V> List<ConsumerRecord<K, V>> fetchRecords(FetchConfig fetchConfig,
                                                       Deserializers<K, V> deserializers,
                                                       int maxRecords) {
            String fetchErrorMessage = "Received exception when fetching the next record from " + partition
                    + ". If needed, please seek past the record to continue consumption.";
            if (corruptLastRecord)
                throw new KafkaException(fetchErrorMessage, cachedRecordException);

            if (isConsumed)
                return Collections.emptyList();

            List<ConsumerRecord<K, V>> records = new ArrayList<>();

            try {
                for (int i = 0; i < maxRecords; i++) {
                    if (cachedRecordException == null) {
                        corruptLastRecord = true;
                        lastRecord = nextFetchedRecord(fetchConfig);
                        corruptLastRecord = false;
                    }

                    if (lastRecord == null)
                        break;

                    Optional<Integer> leaderEpoch = maybeLeaderEpoch(currentBatch.partitionLeaderEpoch());
                    TimestampType timestampType = currentBatch.timestampType();
                    ConsumerRecord<K, V> record = parseRecord(deserializers, partition, leaderEpoch, timestampType, lastRecord);
                    records.add(record);
                    recordsRead++;
                    bytesRead += lastRecord.sizeInBytes();
                    nextFetchOffset = lastRecord.offset() + 1;
                    cachedRecordException = null;
                }
            } catch (SerializationException se) {
                cachedRecordException = se;
                if (records.isEmpty())
                    throw se;
            } catch (KafkaException e) {
                cachedRecordException = e;
                if (records.isEmpty())
                    throw new KafkaException(fetchErrorMessage, e);
            }
            return records;
        }
}
