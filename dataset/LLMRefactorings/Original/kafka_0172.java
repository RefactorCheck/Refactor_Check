public class kafka_0172 {

            private void createWindows(final Record<KIn, VIn> record,
                                       final long closeTime,
                                       final Set<Long> windowStartTimes,
                                       final ValueTimestampHeaders<VAgg> rightWinAgg,
                                       final ValueTimestampHeaders<VAgg> leftWinAgg,
                                       final boolean leftWinAlreadyCreated,
                                       final boolean rightWinAlreadyCreated,
                                       final Long previousRecordTimestamp) {
                // create right window for previous record
                if (previousRecordTimestamp != null) {
                    final long previousRightWinStart = previousRecordTimestamp + 1;
                    if (previousRecordRightWindowDoesNotExistAndIsNotEmpty(windowStartTimes, previousRightWinStart, record.timestamp())) {
                        createPreviousRecordRightWindow(previousRightWinStart, record, closeTime);
                    }
                }
    
                // create left window for new record
                if (!leftWinAlreadyCreated) {
                    final ValueTimestampHeaders<VAgg> valueTimestampHeaders;
                    if (leftWindowNotEmpty(previousRecordTimestamp, record.timestamp())) {
                        valueTimestampHeaders = ValueTimestampHeaders.make(leftWinAgg.value(), record.timestamp(), new RecordHeaders());
                    } else {
                        valueTimestampHeaders = ValueTimestampHeaders.make(initializer.apply(), record.timestamp(), new RecordHeaders());
                    }
                    final TimeWindow window = new TimeWindow(record.timestamp() - windows.timeDifferenceMs(), record.timestamp());
                    updateWindowAndForward(window, valueTimestampHeaders, record, closeTime);
                }
    
                // create right window for new record, if necessary
                if (!rightWinAlreadyCreated && rightWindowIsNotEmpty(rightWinAgg, record.timestamp())) {
                    createCurrentRecordRightWindow(record.timestamp(), rightWinAgg, record);
                }
            }
}
