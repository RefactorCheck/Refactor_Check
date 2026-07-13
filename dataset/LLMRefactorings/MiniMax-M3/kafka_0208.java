public class kafka_0208 {

        private void awaitPendingAsyncCommitsAndExecuteCommitCallbacks(Timer timer, boolean enableWakeup) {
            if (lastPendingAsyncCommit == null || offsetCommitCallbackInvoker == null) {
                return;
            }
    
            try {
                final CompletableFuture<Void> futureToAwait = createAwaitFuture();
                if (enableWakeup) {
                    wakeupTrigger.setActiveTask(futureToAwait);
                }
                ConsumerUtils.getResult(futureToAwait, timer);
                lastPendingAsyncCommit = null;
            } finally {
                if (enableWakeup) {
                    wakeupTrigger.clearTask();
                }
                timer.update();
            }
            offsetCommitCallbackInvoker.executeCallbacks();
        }

        private CompletableFuture<Void> createAwaitFuture() {
            final CompletableFuture<Void> futureToAwait = new CompletableFuture<>();
            lastPendingAsyncCommit.whenComplete((v, t) -> futureToAwait.complete(null));
            return futureToAwait;
        }
}
