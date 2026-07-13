public class kafka_0171 {

        private static MemoryRecordsBuilder buildRetainedRecordsInto(RecordBatch originalBatch,
                                                                     List<Record> retainedRecords,
                                                                     ByteBufferOutputStream bufferOutputStream,
                                                                     final long deleteHorizonMs) {
            Compression compression = Compression.of(originalBatch.compressionType()).build();
            TimestampType timestampType = originalBatch.timestampType() == TimestampType.NO_TIMESTAMP_TYPE ?
                    TimestampType.CREATE_TIME : originalBatch.timestampType();
            long logAppendTime = timestampType == TimestampType.LOG_APPEND_TIME ?
                    originalBatch.maxTimestamp() : RecordBatch.NO_TIMESTAMP;
            long baseOffset = originalBatch.magic() >= RecordBatch.MAGIC_VALUE_V2 ?
                    originalBatch.baseOffset() : retainedRecords.get(0).offset();
            final boolean preserveLastOffset = originalBatch.magic() >= RecordBatch.MAGIC_VALUE_V2;

            MemoryRecordsBuilder builder = new MemoryRecordsBuilder(bufferOutputStream, RecordBatch.CURRENT_MAGIC_VALUE,
                    compression, timestampType, baseOffset, logAppendTime, originalBatch.producerId(),
                    originalBatch.producerEpoch(), originalBatch.baseSequence(), originalBatch.isTransactional(),
                    originalBatch.isControlBatch(), originalBatch.partitionLeaderEpoch(), bufferOutputStream.limit(), deleteHorizonMs);

            for (Record record : retainedRecords)
                builder.append(record);

            if (preserveLastOffset)
                builder.overrideLastOffset(originalBatch.lastOffset());

            return builder;
        }
}
