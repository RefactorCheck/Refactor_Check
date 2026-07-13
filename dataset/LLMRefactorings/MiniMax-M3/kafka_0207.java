public class kafka_0207 {

        protected void iteration() {
            final long offsetCommitIntervalMs = workerConfig.getLong(WorkerConfig.OFFSET_COMMIT_INTERVAL_MS_CONFIG);
    
            try {
                long now = time.milliseconds();
    
                // Maybe commit
                if (!committing && (context.isCommitRequested() || now >= nextCommit)) {
                    commitOffsets(now);
                    nextCommit = now + offsetCommitIntervalMs;
                    context.clearCommitRequest();
                }
    
                checkCommitTimeout(now);
    
                // And process messages
                long timeoutMs = Math.max(nextCommit - now, 0);
                poll(timeoutMs);
            } catch (WakeupException we) {
                log.trace("{} Consumer woken up", this);
    
                if (isStopping())
                    return;
    
                if (shouldPause()) {
                    pauseAll();
                    onPause();
                    context.requestCommit();
                } else if (!pausedForRedelivery) {
                    resumeAll();
                    onResume();
                }
            }
        }
    
        private void checkCommitTimeout(long now) {
            final long commitTimeoutMs = commitStarted + workerConfig.getLong(WorkerConfig.OFFSET_COMMIT_TIMEOUT_MS_CONFIG);
    
            if (committing && now >= commitTimeoutMs) {
                log.warn("{} Commit of offsets timed out", this);
                commitFailures++;
                committing = false;
            }
        }
}
