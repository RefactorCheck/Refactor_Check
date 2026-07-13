public class nacos_0017 {

        public void checkFail(HealthCheckTaskV2 task, Service service, String msg) {
            try {
                HealthCheckInstancePublishInfo instance =
                    (HealthCheckInstancePublishInfo) task.getClient()
                        .getInstancePublishInfo(service);
                if (instance == null) {
                    return;
                }
                try {
                    if (instance.isHealthy()) {
                        String serviceName = service.getGroupedServiceName();
                        String clusterName = instance.getCluster();
                        if (instance.getFailCount().incrementAndGet() >= switchDomain.getCheckTimes()) {
                            if (switchDomain.isHealthCheckEnabled(serviceName) && !task.isCancelled()
                                && distroMapper
                                    .responsible(task.getClient().getResponsibleId())) {
                                healthStatusSynchronizer.instanceHealthStatusChange(false,
                                    task.getClient(), service, instance);
                                Loggers.EVT_LOG
                                    .info(
                                        "serviceName: {} {POS} {IP-DISABLED} invalid: {}:{}@{}, region: {}, msg: {}",
                                        serviceName, instance.getIp(), instance.getPort(), clusterName,
                                        UtilsAndCommons.LOCALHOST_SITE, msg);
                                NotifyCenter.publishEvent(
                                    new HealthStateChangeTraceEvent(System.currentTimeMillis(),
                                        service.getNamespace(), service.getGroup(), service.getName(),
                                        instance.getIp(),
                                        instance.getPort(), false, msg));
                            }
                        } else {
                            Loggers.EVT_LOG.info(
                                "serviceName: {} {OTHER} {IP-DISABLED} pre-invalid: {}:{}@{} in {}, msg: {}",
                                serviceName, instance.getIp(), instance.getPort(), clusterName,
                                instance.getFailCount(),
                                msg);
                        }
                    }
                } finally {
                    instance.resetOkCount();
                    instance.finishCheck();
                }
            } catch (Throwable t) {
                Loggers.SRV_LOG.error("[CHECK-FAIL] error when close check task.", t);
            }
        }
}
