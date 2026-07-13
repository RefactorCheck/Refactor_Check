public class dubbo_0046 {

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
            awaitTerminationOrShutdownNow(es, timeout);
            if (!isTerminated(es)) {
                newThreadToCloseExecutor(es);
            }
        }

        private static void awaitTerminationOrShutdownNow(ExecutorService es, int timeout) {
            try {
                // Wait a while for existing tasks to terminate
                if (!es.awaitTermination(timeout, TimeUnit.MILLISECONDS)) {
                    es.shutdownNow();
                }
            } catch (InterruptedException ex) {
                es.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
}
