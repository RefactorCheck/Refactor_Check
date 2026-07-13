public class kafka_0222 {

    private void updateWindowAndForward(final Window window,
                                        final ValueTimestampHeaders<VAgg> valueTimestampHeaders,
                                        final Record<KIn, VIn> record,
                                        final long windowCloseTime) {
        final long windowStart = window.start();
        final long windowEnd = window.end();

        if (windowEnd >= windowCloseTime) {
            updateActiveWindow(windowStart, windowEnd, valueTimestampHeaders, record);
        } else {
            logExpiredWindow(windowStart, windowEnd, record, windowCloseTime);
        }
    }

    private void updateActiveWindow(final long windowStart,
                                    final long windowEnd,
                                    final ValueTimestampHeaders<VAgg> valueTimestampHeaders,
                                    final Record<KIn, VIn> record) {
        final VAgg oldAgg = getValueOrNull(valueTimestampHeaders);
        final VAgg newAgg = aggregator.apply(record.key(), record.value(), oldAgg);

        final long newTimestamp = oldAgg == null ? record.timestamp() : Math.max(record.timestamp(), valueTimestampHeaders.timestamp());
        windowStore.put(
            record.key(),
            ValueTimestampHeaders.make(newAgg, newTimestamp, new RecordHeaders()),
            windowStart);
        maybeForwardUpdate(record, window, oldAgg, newAgg, newTimestamp);
    }

    private void logExpiredWindow(final long windowStart,
                                  final long windowEnd,
                                  final Record<KIn, VIn> record,
                                  final long windowCloseTime) {
        final String windowString = "[" + windowStart + "," + windowEnd + "]";
        logSkippedRecordForExpiredWindow(log, record.timestamp(), windowCloseTime, windowString);
    }
}
