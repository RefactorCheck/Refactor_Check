public class kafka_0107 {

            @Override
            public FetchDataInfo read(long startOffset, int maxLength, FetchIsolation isolation, boolean minOneMessage) {
                if (startOffset < 0 || startOffset >= logEndOffset()) {
                    return new FetchDataInfo(new LogOffsetMetadata(startOffset), MemoryRecords.EMPTY);
                }

                long patternLength = batch.length + 1;
                MemoryRecords records = startOffset % patternLength < batch.length
                        ? createBatchRecords(startOffset)
                        : createCommitMarkerRecords(startOffset);
                return new FetchDataInfo(new LogOffsetMetadata(startOffset), records);
            }

            private MemoryRecords createBatchRecords(long startOffset) {
                return MemoryRecords.withTransactionalRecords(
                    startOffset,
                    Compression.NONE,
                    producerId,
                    producerEpoch,
                    0,
                    0,
                    batch
                );
            }

            private MemoryRecords createCommitMarkerRecords(long startOffset) {
                return MemoryRecords.withEndTransactionMarker(
                    startOffset,
                    0L,
                    0,
                    producerId,
                    producerEpoch,
                    new EndTransactionMarker(ControlRecordType.COMMIT, coordinatorEpoch)
                );
            }
}
