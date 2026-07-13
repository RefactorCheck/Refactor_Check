public class kafka_0107 {

            @Override
            public FetchDataInfo read(long startOffset, int maxLength, FetchIsolation isolation, boolean minOneMessage) {
                if (startOffset < 0 || startOffset >= logEndOffset()) {
                    return new FetchDataInfo(new LogOffsetMetadata(startOffset), MemoryRecords.EMPTY);
                }

                long batchAndMarkerLength = batch.length + 1;
                if (startOffset % batchAndMarkerLength < batch.length) {
                    MemoryRecords records = MemoryRecords.withTransactionalRecords(
                        startOffset,
                        Compression.NONE,
                        producerId,
                        producerEpoch,
                        0,
                        0,
                        batch
                    );
                    return new FetchDataInfo(new LogOffsetMetadata(startOffset), records);
                } else {
                    MemoryRecords records = MemoryRecords.withEndTransactionMarker(
                        startOffset,
                        0L,
                        0,
                        producerId,
                        producerEpoch,
                        new EndTransactionMarker(ControlRecordType.COMMIT, coordinatorEpoch)
                    );
                    return new FetchDataInfo(new LogOffsetMetadata(startOffset), records);
                }
            }
}
