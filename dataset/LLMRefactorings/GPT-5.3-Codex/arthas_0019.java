public class arthas_0019 {

        public static long findTcpListenProcessRefactored(int port) {
            // Add a timeout of 5 seconds to prevent blocking
            final int TIMEOUT_SECONDS = 5;
            ExecutorService executor = Executors.newSingleThreadExecutor();
            try {
                Future<Long> future = executor.submit(new Callable<Long>() {
                    @Override
                    public Long call() throws Exception {
                        return doFindTcpListenProcess(port);
                    }
                });
    
                try {
                    return future.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
                } catch (TimeoutException e) {
                    future.cancel(true);
                    return -1;
                } catch (Exception e) {
                    return -1;
                }
            } finally {
                executor.shutdownNow();
            }
        }
}
