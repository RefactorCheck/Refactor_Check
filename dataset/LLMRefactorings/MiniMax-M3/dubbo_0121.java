public class dubbo_0121 {

        public DubboBootstrap await() {
            // if has been waited, no need to wait again, return immediately
            if (!awaited.get()) {
                if (!isStopped()) {
                    executeMutually(this::awaitCondition);
                }
            }
            return this;
        }

        private void awaitCondition() {
            while (!awaited.get()) {
                if (logger.isInfoEnabled()) {
                    logger.info(NAME + " awaiting ...");
                }
                try {
                    condition.await();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
}
