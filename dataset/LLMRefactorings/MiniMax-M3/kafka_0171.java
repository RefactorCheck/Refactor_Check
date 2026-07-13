public class kafka_0171 {

        private static MemoryRecordsBuilder buildRetainedRecordsInto(RecordBatch originalBatch,
                                                                     List<Record> retainedRecords,
                                                                     ByteBufferOutputStream bufferOutputStream,
                                                                     final long deleteHorizonMs) {
            Compression compression = Compression.of(originalBatch.compressionType()).build();
            TimestampType timestampType = resolveTimestampType(originalBatch);
            long logAppendTime = resolveLogAppendTime(originalBatch, timestampType);
            long baseOffset = resolveBaseOffset(originalBatch, retainedRecords);

            MemoryRecordsBuilder builder = new MemoryRecordsBuilder(bufferOutputStream, RecordBatch.CURRENT_MAGIC_VALUE,
                    compression, timestampType, baseOffset, logAppendTime, originalBatch.producerId(),
                    originalBatch.producerEpoch(), originalBatch.baseSequence(), originalBatch.isTransactional(),
                    originalBatch.isControlBatch(), originalBatch.partitionLeaderEpoch(), bufferOutputStream.limit(), deleteHorizonMs);

            for (Record record : retainedRecords)
                builder.append(record);

            if (originalBatch.magic() >= RecordBatch.MAGIC_VALUE_V2)
                builder.overrideLastOffset(originalBatch.lastOffset());

            return builder;
        }

        // V0 has no timestamp type or timestamp, so we set the timestamp to CREATE_TIME and timestamp to NO_TIMESTAMP.
        // Note that this differs from produce up-conversion where the timestamp type topic config is used and the log append
        // time is generated if the config is LOG_APPEND_TIME. The reason for the different behavior is that there is
        // no appropriate log append time we can generate at compaction time.
        private static TimestampType resolveTimestampType(RecordBatch originalBatch) {
            return originalBatch.timestampType() == TimestampType.NO_TIMESTAMP_TYPE ?
                    TimestampType.CREATE_TIME : originalBatch.timestampType();
        }

        private static long resolveLogAppendTime(RecordBatch originalBatch, TimestampType timestampType) {
            return timestampType == TimestampType.LOG_APPEND_TIME ?
                    originalBatch.maxTimestamp() : RecordBatch.NO_TIMESTAMP;
        }

        private static long resolveBaseOffset(RecordBatch originalBatch, List<Record> retainedRecords) {
            return originalBatch.magic() >= RecordBatch.MAGIC_VALUE_V2 ?
                    originalBatch.baseOffset() : retainedRecords.get(0).offset();
        }
}
