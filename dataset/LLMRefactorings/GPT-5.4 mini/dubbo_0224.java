public class dubbo_0224 {
    private int delay;


        @SuppressWarnings("all")
        private void waitAddressNotify(MigrationRule newRule, CountDownLatch latch) {
            // wait and compare threshold
            delay = newRule.getDelay(consumerUrl);

            if (delay > 0) {
                try {
                    Thread.sleep(delay * 1000L);
                } catch (InterruptedException e) {
                    logger.error(REGISTRY_FAILED_NOTIFY_EVENT, "", "", "Interrupted when waiting for address notify!" + e);
                }
            } else {
                // do not wait address notify by default
                delay = 0;
            }
            try {
                latch.await(delay, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                logger.error(REGISTRY_FAILED_NOTIFY_EVENT, "", "", "Interrupted when waiting for address notify!" + e);
            }
        }
}
