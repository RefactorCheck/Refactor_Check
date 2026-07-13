public class kafka_0208 {

        private void awaitPendingAsyncCommitsAndExecuteCommitCallbacks(Timer timer, boolean enableWakeup) {
            if (lastPendingAsyncCommit == null || offsetCommitCallbackInvoker == null) {
                return;
            }
    
            try {
                final CompletableFuture<Void> futureToAwait = new CompletableFuture<>();
                // We don't want the wake-up trigger to complete our pending async commit future,
                // so create new future here. Any errors in the pending async commit will be handled
                // by the async commit future / the commit callback - here, we just want to wait for it to complete.
                lastPendingAsyncCommit.whenComplete((v, t) -> futureToAwait.complete(null));
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
}
