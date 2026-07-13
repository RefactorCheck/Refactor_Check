public class kafka_0172 {

            private void createWindows(final Record<KIn, VIn> record,
                                       final long closeTime,
                                       final Set<Long> windowStartTimes,
                                       final ValueTimestampHeaders<VAgg> rightWinAgg,
                                       final ValueTimestampHeaders<VAgg> leftWinAgg,
                                       final boolean leftWinAlreadyCreated,
                                       final boolean rightWinAlreadyCreated,
                                       final Long previousRecordTimestamp) {
                createPreviousRecordRightWindowIfNeeded(record, closeTime, windowStartTimes, previousRecordTimestamp);
                createLeftWindowForNewRecord(record, closeTime, leftWinAgg, leftWinAlreadyCreated, previousRecordTimestamp);
                createRightWindowForNewRecordIfNeeded(record, rightWinAgg, rightWinAlreadyCreated);
            }

            private void createPreviousRecordRightWindowIfNeeded(final Record<KIn, VIn> record,
                                                                  final long closeTime,
                                                                  final Set<Long> windowStartTimes,
                                                                  final Long previousRecordTimestamp) {
                if (previousRecordTimestamp != null) {
                    final long previousRightWinStart = previousRecordTimestamp + 1;
                    if (previousRecordRightWindowDoesNotExistAndIsNotEmpty(windowStartTimes, previousRightWinStart, record.timestamp())) {
                        createPreviousRecordRightWindow(previousRightWinStart, record, closeTime);
                    }
                }
            }

            private void createLeftWindowForNewRecord(final Record<KIn, VIn> record,
                                                      final long closeTime,
                                                      final ValueTimestampHeaders<VAgg> leftWinAgg,
                                                      final boolean leftWinAlreadyCreated,
                                                      final Long previousRecordTimestamp) {
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
            }

            private void createRightWindowForNewRecordIfNeeded(final Record<KIn, VIn> record,
                                                                final ValueTimestampHeaders<VAgg> rightWinAgg,
                                                                final boolean rightWinAlreadyCreated) {
                if (!rightWinAlreadyCreated && rightWindowIsNotEmpty(rightWinAgg, record.timestamp())) {
                    createCurrentRecordRightWindow(record.timestamp(), rightWinAgg, record);
                }
            }
}
