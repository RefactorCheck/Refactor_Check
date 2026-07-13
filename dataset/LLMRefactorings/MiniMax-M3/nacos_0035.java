public class nacos_0035 {

        private static ServiceInfo doSelectInstances(ServiceInfo serviceInfo, String cluster,
            boolean healthyOnly, boolean enableOnly,
            InstancesFilter filter) {
            ServiceInfo result = new ServiceInfo();
            result.setName(serviceInfo.getName());
            result.setGroupName(serviceInfo.getGroupName());
            result.setCacheMillis(serviceInfo.getCacheMillis());
            result.setLastRefTime(System.currentTimeMillis());
            result.setClusters(cluster);
            result.setReachProtectionThreshold(false);
            Set<String> clusterSets = parseClusters(cluster);
            long healthyCount = 0L;
            // The instance list won't be modified almost time.
            List<com.alibaba.nacos.api.naming.pojo.Instance> filteredInstances = new LinkedList<>();
            // The instance list of all filtered by cluster/enabled condition.
            List<com.alibaba.nacos.api.naming.pojo.Instance> allInstances = new LinkedList<>();
            for (com.alibaba.nacos.api.naming.pojo.Instance ip : serviceInfo.getHosts()) {
                if (checkCluster(clusterSets, ip) && checkEnabled(enableOnly, ip)) {
                    if (!healthyOnly || ip.isHealthy()) {
                        filteredInstances.add(ip);
                    }
                    if (ip.isHealthy()) {
                        healthyCount += 1;
                    }
                    allInstances.add(ip);
                }
            }
            result.setHosts(filteredInstances);
            if (filter != null) {
                filter.doFilter(result, allInstances, healthyCount);
            }
            return result;
        }

        private static Set<String> parseClusters(String cluster) {
            return com.alibaba.nacos.common.utils.StringUtils.isNotBlank(cluster)
                ? new HashSet<>(Arrays.asList(cluster.split(","))) : new HashSet<>();
        }
}
