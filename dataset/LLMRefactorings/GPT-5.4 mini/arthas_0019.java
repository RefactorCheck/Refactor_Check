public class arthas_0019 {

        public static long findTcpListenProcess(int port) {
            // Add a timeout of 5 seconds to prevent blocking
            ExecutorService executor = Executors.newSingleThreadExecutor();
            try {
                Future<Long> future = executor.submit(new Callable<Long>() {
                    @Override
                    public Long call() throws Exception {
                        return doFindTcpListenProcess(port);
                    }
                });
    
                try {
                    return future.get(5, TimeUnit.SECONDS);
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
