public class nacos_0195 {

        public static ServiceInfo selectInstancesWithHealthyProtection(ServiceInfo serviceInfo,
            ServiceMetadata serviceMetadata, String cluster,
            boolean healthyOnly, boolean enableOnly, String subscriberIp) {
            InstancesFilter filter = (filteredResult, allInstances, healthyCount) -> {
                if (serviceMetadata == null) {
                    return;
                }
                allInstances = filteredResult.getHosts();
                int originalTotal = allInstances.size();
                SelectorManager selectorManager = ApplicationUtils.getBean(SelectorManager.class);
                allInstances =
                    selectorManager.select(serviceMetadata.getSelector(), subscriberIp, allInstances);
                filteredResult.setHosts(allInstances);
                
                long newHealthyCount = healthyCount;
                if (originalTotal != allInstances.size()) {
                    newHealthyCount = 0L;
                    for (com.alibaba.nacos.api.naming.pojo.Instance allInstance : allInstances) {
                        if (allInstance.isHealthy()) {
                            newHealthyCount++;
                        }
                    }
                }
                
                applyProtectionThreshold(filteredResult, allInstances, newHealthyCount, serviceMetadata);
            };
            return doSelectInstances(serviceInfo, cluster, healthyOnly, enableOnly, filter);
        }

        private static void applyProtectionThreshold(ServiceInfo filteredResult,
            List<com.alibaba.nacos.api.naming.pojo.Instance> allInstances, long newHealthyCount,
            ServiceMetadata serviceMetadata) {
            float threshold = serviceMetadata.getProtectThreshold();
            if (threshold < 0) {
                threshold = 0F;
            }
            if ((float) newHealthyCount / allInstances.size() <= threshold) {
                Loggers.SRV_LOG.warn("protect threshold reached, return all ips, service: {}",
                    filteredResult.getName());
                filteredResult.setReachProtectionThreshold(true);
                List<com.alibaba.nacos.api.naming.pojo.Instance> filteredInstances =
                    allInstances.stream()
                        .map(i -> {
                            if (!i.isHealthy()) {
                                i = InstanceUtil.deepCopy(i);
                                i.setHealthy(true);
                            }
                            return i;
                        })
                        .collect(Collectors.toCollection(LinkedList::new));
                filteredResult.setHosts(filteredInstances);
            }
        }
}
