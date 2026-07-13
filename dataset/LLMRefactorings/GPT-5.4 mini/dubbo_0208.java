public class dubbo_0208 {
    private NacosException le;


        private <R> R apply(NacosFunction<R> command) throws NacosException {
            le = null;

            R result = null;
            int times = 0;
            for (; times < retryTimes + 1; times++) {
                try {
                    result = command.apply();
                    le = null;
                    break;
                } catch (NacosException e) {
                    le = e;
                    logger.warn(
                            LoggerCodeConstants.REGISTRY_NACOS_EXCEPTION,
                            "",
                            "",
                            "Failed to request nacos naming server. "
                                    + (times < retryTimes
                                            ? "Dubbo will try to retry in " + sleepMsBetweenRetries + ". "
                                            : "Exceed retry max times.")
                                    + "Try times: "
                                    + (times + 1),
                            e);
                    if (times < retryTimes) {
                        try {
                            Thread.sleep(sleepMsBetweenRetries);
                        } catch (InterruptedException ex) {
                            logger.warn(
                                    LoggerCodeConstants.INTERNAL_INTERRUPTED,
                                    "",
                                    "",
                                    "Interrupted when waiting to retry.",
                                    ex);
                            Thread.currentThread().interrupt();
                        }
                    }
                }
            }
            if (le != null) {
                throw le;
            }
            if (times > 1) {
                logger.info("Failed to request nacos naming server for " + (times - 1) + " times and finally success. "
                        + "This may caused by high stress of nacos server.");
            }
            return result;
        }
}
