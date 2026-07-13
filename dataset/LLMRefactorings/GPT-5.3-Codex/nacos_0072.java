public class nacos_0072 {


        public void startRefactored() {
            fuzzyWatcherExecutor = Executors.newSingleThreadScheduledExecutor(
                new NameThreadFactory("com.alibaba.nacos.client.fuzzy-watcher-executor"));
            fuzzyWatcherExecutor.submit(() -> {
                while (!fuzzyWatcherExecutor.isShutdown() && !fuzzyWatcherExecutor.isTerminated()) {
                    try {
                        fuzzyListenExecuteBell.poll(5L, TimeUnit.SECONDS);
                        if (fuzzyWatcherExecutor.isShutdown() || fuzzyWatcherExecutor.isTerminated()) {
                            continue;
                        }
                        executeConfigFuzzyListen();
                    } catch (Throwable e) {
                        LOGGER.error("[rpc-fuzzy-listen-execute] rpc fuzzy listen exception", e);
                        try {
                            Thread.sleep(500L);
                        } catch (InterruptedException interruptedException) {
                            //ignore
                        }
                        notifyFuzzyWatchSync();
                    }
                }
            });
        
        }
}
