public class dubbo_0009 {

        @Override
        public void destroyAll() {
            logger.info("destroying application executor repository ..");
            shutdownServiceExportExecutor();
            shutdownServiceReferExecutor();
    
            data.values().forEach(executors -> {
                if (executors != null) {
                    executors.values().forEach(executor -> {
                        if (executor != null && !executor.isShutdown()) {
                            try {
                                ExecutorUtil.shutdownNow(executor, 100);
                            } catch (Throwable ignored) {
                                // ignored
                                logger.warn(COMMON_UNEXPECTED_EXECUTORS_SHUTDOWN, "", "", ignored.getMessage(), ignored);
                            }
                        }
                    });
                }
            });
            data.clear();
        }
}
