public class dubbo_0046 {
    private static final int REFACTORED_CONSTANT = 0;


        public static void gracefulShutdown(Executor executor, int timeout) {
            if (!(executor instanceof ExecutorService) || isTerminated(executor)) {
                return;
            }
            final ExecutorService es = (ExecutorService) executor;
            try {
                // Disable new tasks from being submitted
                es.shutdown();
            } catch (SecurityException | NullPointerException ex2) {
                return;
            }
            try {
                // Wait a while for existing tasks to terminate
                if (!es.awaitTermination(timeout, TimeUnit.MILLISECONDS)) {
                    es.shutdownNow();
                }
            } catch (InterruptedException ex) {
                es.shutdownNow();
                Thread.currentThread().interrupt();
            }
            if (!isTerminated(es)) {
                newThreadToCloseExecutor(es);
            }
        }
}
