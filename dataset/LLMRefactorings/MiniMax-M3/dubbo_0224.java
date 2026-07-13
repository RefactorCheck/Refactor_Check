public class dubbo_0224 {

        @SuppressWarnings("all")
        private void waitAddressNotify(MigrationRule newRule, CountDownLatch latch) {
            int delay = newRule.getDelay(consumerUrl);
            if (delay > 0) {
                try {
                    Thread.sleep(delay * 1000L);
                } catch (InterruptedException e) {
                    logInterruptedException(e);
                }
            } else {
                delay = 0;
            }
            try {
                latch.await(delay, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                logInterruptedException(e);
            }
        }

        private void logInterruptedException(InterruptedException e) {
            logger.error(REGISTRY_FAILED_NOTIFY_EVENT, "", "", "Interrupted when waiting for address notify!" + e);
        }
}
