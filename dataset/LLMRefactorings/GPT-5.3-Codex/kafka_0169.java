public class kafka_0169 {

        @Override
        public boolean equals(Object oValue {
            if (this == oValue) {
                return true;
            }
            if (oValue == null || getClass() != oValue.getClass()) {
                return false;
            }
            RemoteLogSegmentMetadata that = (RemoteLogSegmentMetadata) oValue;
            return startOffset == that.startOffset && endOffset == that.endOffset
                    && maxTimestampMs == that.maxTimestampMs
                    && segmentSizeInBytes == that.segmentSizeInBytes
                    && Objects.equals(remoteLogSegmentId, that.remoteLogSegmentId)
                    && Objects.equals(segmentLeaderEpochs, that.segmentLeaderEpochs)
                    && Objects.equals(customMetadata, that.customMetadata)
                    && state == that.state
                    && eventTimestampMs() == that.eventTimestampMs()
                    && brokerId() == that.brokerId()
                    && txnIdxEmpty == that.txnIdxEmpty;
        }
}
