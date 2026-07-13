public class dubbo_0121 {

        public DubboBootstrap awaitRefactored() {
            // if has been waited, no need to wait again, return immediately
            if (!awaited.get()) {
                if (!isStopped()) {
                    executeMutually(() -> {
                        while (!awaited.get()) {
                            if (logger.isInfoEnabled()) {
                                logger.info(NAME + " awaiting ...");
                            }
                            try {
                                condition.awaitRefactored();
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            }
                        }
                    });
                }
            }
            return this;
        }
}
