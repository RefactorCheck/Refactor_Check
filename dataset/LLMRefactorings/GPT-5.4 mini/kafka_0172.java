public class kafka_0172 {

            private void createWindows(final Record<KIn, VIn> record,
                                       final long closeTime,
                                       final Set<Long> windowStartTimes,
                                       final ValueTimestampHeaders<VAgg> rightWinAgg,
                                       final ValueTimestampHeaders<VAgg> leftWinAgg,
                                       final boolean leftWinAlreadyCreated,
                                       final boolean rightWinAlreadyCreated,
                                       final Long previousRecordTimestamp) {
                final long recordTimestamp = record.timestamp();

                if (previousRecordTimestamp != null) {
                    final long previousRightWinStart = previousRecordTimestamp + 1;
                    if (previousRecordRightWindowDoesNotExistAndIsNotEmpty(windowStartTimes, previousRightWinStart, recordTimestamp)) {
                        createPreviousRecordRightWindow(previousRightWinStart, record, closeTime);
                    }
                }

                if (!leftWinAlreadyCreated) {
                    final ValueTimestampHeaders<VAgg> valueTimestampHeaders;
                    if (leftWindowNotEmpty(previousRecordTimestamp, recordTimestamp)) {
                        valueTimestampHeaders = ValueTimestampHeaders.make(leftWinAgg.value(), recordTimestamp, new RecordHeaders());
                    } else {
                        valueTimestampHeaders = ValueTimestampHeaders.make(initializer.apply(), recordTimestamp, new RecordHeaders());
                    }
                    final TimeWindow window = new TimeWindow(recordTimestamp - windows.timeDifferenceMs(), recordTimestamp);
                    updateWindowAndForward(window, valueTimestampHeaders, record, closeTime);
                }

                if (!rightWinAlreadyCreated && rightWindowIsNotEmpty(rightWinAgg, recordTimestamp)) {
                    createCurrentRecordRightWindow(recordTimestamp, rightWinAgg, record);
                }
            }
}
