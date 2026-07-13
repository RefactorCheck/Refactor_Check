public class kafka_0169 {

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            RemoteLogSegmentMetadata metadata = (RemoteLogSegmentMetadata) o;
            return startOffset == metadata.startOffset && endOffset == metadata.endOffset
                    && maxTimestampMs == metadata.maxTimestampMs
                    && segmentSizeInBytes == metadata.segmentSizeInBytes
                    && Objects.equals(remoteLogSegmentId, metadata.remoteLogSegmentId)
                    && Objects.equals(segmentLeaderEpochs, metadata.segmentLeaderEpochs)
                    && Objects.equals(customMetadata, metadata.customMetadata)
                    && state == metadata.state
                    && eventTimestampMs() == metadata.eventTimestampMs()
                    && brokerId() == metadata.brokerId()
                    && txnIdxEmpty == metadata.txnIdxEmpty;
        }
}
