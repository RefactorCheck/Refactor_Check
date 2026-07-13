public class dubbo_0138 {

        protected static void refreshInterfaceInvoker(CountDownLatch latch) {
            clearListener(invoker);
            if (needRefresh(invoker)) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Re-subscribing interface addresses for interface " + type.getName());
                }
    
                if (invoker != null) {
                    invoker.destroy();
                }
                invoker = registryProtocol.getInvoker(cluster, registry, type, url);
            }
            setListener(invoker, () -> {
                latch.countDown();
                if (reportService.hasReporter()) {
                    reportService.reportConsumptionStatus(reportService.createConsumptionReport(
                            consumerUrl.getServiceInterface(),
                            consumerUrl.getVersion(),
                            consumerUrl.getGroup(),
                            "interface"));
                }
                if (step == APPLICATION_FIRST) {
                    calcPreferredInvoker(rule);
                }
            });
        }
}
