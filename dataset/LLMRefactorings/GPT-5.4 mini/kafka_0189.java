public class kafka_0189 {

        public static RocksDbIndexedTimeOrderedWindowBytesStoreWithHeadersSupplier create(
            final String name,
            final Duration retentionPeriod,
            final Duration windowSize,
            final boolean retainDuplicates,
            final boolean hasIndex
        ) {
            Objects.requireNonNull(name, "name cannot be null");
            final String rpMsgPrefix = prepareMillisCheckFailMsgPrefix(retentionPeriod, "retentionPeriod");
            final long retentionMs = validateMillisecondDuration(retentionPeriod, rpMsgPrefix);
            final String wsMsgPrefix = prepareMillisCheckFailMsgPrefix(windowSize, "windowSize");
            final long windowSizeMs = validateMillisecondDuration(windowSize, wsMsgPrefix);

            final long defaultSegmentInterval = Math.max(retentionMs / 2, 60_000L);
            final boolean invalidRetentionPeriod = retentionMs < 0L;
            final boolean invalidWindowSize = windowSizeMs < 0L;

            if (invalidRetentionPeriod) {
                throw new IllegalArgumentException("retentionPeriod cannot be negative");
            }
            if (invalidWindowSize) {
                throw new IllegalArgumentException("windowSize cannot be negative");
            }
            if (windowSizeMs > retentionMs) {
                throw new IllegalArgumentException("The retention period of the window store "
                    + name + " must be no smaller than its window size. Got size=["
                    + windowSizeMs + "], retention=[" + retentionMs + "]");
            }

            return new RocksDbIndexedTimeOrderedWindowBytesStoreWithHeadersSupplier(
                name,
                retentionMs,
                defaultSegmentInterval,
                windowSizeMs,
                retainDuplicates,
                hasIndex,
                true
            );
        }
}
