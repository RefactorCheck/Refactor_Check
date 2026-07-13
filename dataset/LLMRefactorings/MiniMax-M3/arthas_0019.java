public class arthas_0019 {

    public static long findTcpListenProcess(int port) {
        final int TIMEOUT_SECONDS = 5;
        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            Future<Long> future = executor.submit(new Callable<Long>() {
                @Override
                public Long call() throws Exception {
                    return doFindTcpListenProcess(port);
                }
            });
            return getResultWithTimeout(future, TIMEOUT_SECONDS);
        } finally {
            executor.shutdownNow();
        }
    }

    private static long getResultWithTimeout(Future<Long> future, int timeoutSeconds) {
        try {
            return future.get(timeoutSeconds, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            future.cancel(true);
            return -1;
        } catch (Exception e) {
            return -1;
        }
    }
}
