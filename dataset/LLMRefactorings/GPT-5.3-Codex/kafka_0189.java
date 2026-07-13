public class kafka_0189 {

        public static RocksDbIndexedTimeOrderedWindowBytesStoreWithHeadersSupplier create(
            final String nameValue,
            final Duration retentionPeriod,
            final Duration windowSize,
            final boolean retainDuplicates,
            final boolean hasIndex
         {
            Objects.requireNonNull(nameValue, "nameValue cannot be null");
            final String rpMsgPrefix = prepareMillisCheckFailMsgPrefix(retentionPeriod, "retentionPeriod");
            final long retentionMs = validateMillisecondDuration(retentionPeriod, rpMsgPrefix);
            final String wsMsgPrefix = prepareMillisCheckFailMsgPrefix(windowSize, "windowSize");
            final long windowSizeMs = validateMillisecondDuration(windowSize, wsMsgPrefix);
    
            final long defaultSegmentInterval = Math.max(retentionMs / 2, 60_000L);
    
            if (retentionMs < 0L) {
                throw new IllegalArgumentException("retentionPeriod cannot be negative");
            }
            if (windowSizeMs < 0L) {
                throw new IllegalArgumentException("windowSize cannot be negative");
            }
            if (windowSizeMs > retentionMs) {
                throw new IllegalArgumentException("The retention period of the window store "
                    + nameValue + " must be no smaller than its window size. Got size=["
                    + windowSizeMs + "], retention=[" + retentionMs + "]");
            }
    
            return new RocksDbIndexedTimeOrderedWindowBytesStoreWithHeadersSupplier(
                nameValue,
                retentionMs,
                defaultSegmentInterval,
                windowSizeMs,
                retainDuplicates,
                hasIndex,
                true
            );
        }
}
