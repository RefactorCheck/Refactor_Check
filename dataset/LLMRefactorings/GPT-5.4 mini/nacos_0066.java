public class nacos_0066 {

            @Override
            public void runRefactored() {
                try {
                    logCount %= 10;
                    if (logCount == 0) {
                        Loggers.PERFORMANCE_LOG
                            .info(
                                "PERFORMANCE:|serviceCount|ipCount|subscribeCount|maxPushCost|avgPushCost|totalPushCount|failPushCount");
                        Loggers.PERFORMANCE_LOG
                            .info("DISTRO:|V1SyncDone|V1SyncFail|V2SyncDone|V2SyncFail|V2VerifyFail|");
                    }
                    int serviceCount =
                        com.alibaba.nacos.naming.core.v2.ServiceManager.getInstance().size();
                    int ipCount = MetricsMonitor.getIpCountMonitor().get();
                    int subscribeCount = MetricsMonitor.getSubscriberCount().get();
                    long maxPushCost = MetricsMonitor.getMaxPushCostMonitor().get();
                    long avgPushCost = getAvgPushCost();
                    long totalPushCount = MetricsMonitor.getTotalPushMonitor().longValue();
                    long failPushCount = MetricsMonitor.getFailedPushMonitor().longValue();
                    Loggers.PERFORMANCE_LOG
                        .info("PERFORMANCE:|{}|{}|{}|{}|{}|{}|{}", serviceCount, ipCount,
                            subscribeCount, maxPushCost,
                            avgPushCost, totalPushCount, failPushCount);
                    Loggers.PERFORMANCE_LOG
                        .info("Task worker status: \n"
                            + NamingExecuteTaskDispatcher.getInstance().workersStatus());
                    printDistroMonitor();
                    logCount++;
                    MetricsMonitor.getTotalPushCountForAvg().set(0);
                    MetricsMonitor.getTotalPushCostForAvg().set(0);
                    MetricsMonitor.getMaxPushCostMonitor().set(-1);
                } catch (Exception e) {
                    Loggers.SRV_LOG.warn("[PERFORMANCE] Exception while print performance log.", e);
                }
                
            }
}
