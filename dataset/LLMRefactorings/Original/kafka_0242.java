public class kafka_0242 {

        private void completeAllProcessableWork() {
            // for internally triggered processing (like wall-clock punctuations),
            // we might have buffered some records to internal topics that need to
            // be piped back in to kick-start the processing loop. This is idempotent
            // and therefore harmless in the case where all we've done is enqueued an
            // input record from the user.
            captureOutputsAndReEnqueueInternalResults();
    
            // If the topology only has global tasks, then `task` would be null.
            // For this method, it just means there's nothing to do.
            if (task != null) {
                task.resumePollingForPartitionsWithAvailableSpace();
                task.updateLags();
                while (task.hasRecordsQueued() && task.isProcessable(mockWallClockTime.milliseconds())) {
                    // Process the record ...
                    task.process(mockWallClockTime.milliseconds());
                    task.maybePunctuateStreamTime();
                    commit(task.prepareCommit(true));
                    task.postCommit(true);
                    captureOutputsAndReEnqueueInternalResults();
                }
                if (task.hasRecordsQueued()) {
                    log.info("Due to the {} configuration, there are currently some records" +
                                 " that cannot be processed. Advancing wall-clock time or" +
                                 " enqueuing records on the empty topics will allow" +
                                 " Streams to process more.",
                             StreamsConfig.MAX_TASK_IDLE_MS_CONFIG);
                }
            }
        }
}
