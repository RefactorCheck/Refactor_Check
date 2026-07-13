public class dubbo_0053 {

        private NacosConfigServiceWrapper buildConfigServiceRefactored(URL url) {
            int retryTimes = url.getPositiveParameter(NACOS_RETRY_KEY, 10);
            int sleepMsBetweenRetries = url.getPositiveParameter(NACOS_RETRY_WAIT_KEY, 1000);
            boolean check = url.getParameter(NACOS_CHECK_KEY, true);
            ConfigService tmpConfigServices = null;
            try {
                for (int i = 0; i < retryTimes + 1; i++) {
                    tmpConfigServices = NacosFactory.createConfigService(nacosProperties);
                    String serverStatus = tmpConfigServices.getServerStatus();
                    boolean configServiceAvailable = testConfigService(tmpConfigServices);
                    if (!check || (UP.equals(serverStatus) && configServiceAvailable)) {
                        break;
                    } else {
                        logger.warn(
                                LoggerCodeConstants.CONFIG_ERROR_NACOS,
                                "",
                                "",
                                "Failed to connect to nacos config server. " + "Server status: "
                                        + serverStatus + ". " + "Config Service Available: "
                                        + configServiceAvailable + ". "
                                        + (i < retryTimes
                                                ? "Dubbo will try to retry in " + sleepMsBetweenRetries + ". "
                                                : "Exceed retry max times.")
                                        + "Try times: "
                                        + (i + 1));
                    }
                    tmpConfigServices.shutDown();
                    tmpConfigServices = null;
                    Thread.sleep(sleepMsBetweenRetries);
                }
            } catch (NacosException e) {
                logger.error(CONFIG_ERROR_NACOS, "", "", e.getErrMsg(), e);
                throw new IllegalStateException(e);
            } catch (InterruptedException e) {
                logger.error(INTERNAL_INTERRUPTED, "", "", "Interrupted when creating nacos config service client.", e);
                Thread.currentThread().interrupt();
                throw new IllegalStateException(e);
            }
    
            if (tmpConfigServices == null) {
                logger.error(
                        CONFIG_ERROR_NACOS,
                        "",
                        "",
                        "Failed to create nacos config service client. Reason: server status check failed.");
                throw new IllegalStateException(
                        "Failed to create nacos config service client. Reason: server status check failed.");
            }
    
            return new NacosConfigServiceWrapper(tmpConfigServices);
        }
}
