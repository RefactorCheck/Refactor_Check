public class nacos_0135 {

        private Result registerCluster(String serviceName, String productName, String clusterName,
            String ips)
            throws NacosException {
            String serviceWithoutGroup = NamingUtils.getServiceName(serviceName);
            String groupName = NamingUtils.getGroupName(serviceName);
            Service service = Service.newService(Constants.DEFAULT_NAMESPACE_ID, groupName,
                serviceWithoutGroup, false);
            service = ServiceManager.getInstance().getSingleton(service);
            if (service.isEphemeral()) {
                return new Result(
                    String.format("Service %s is ephemeral service, can't use as address server",
                        serviceName),
                    0);
            }
            ServiceMetadata serviceMetadata =
                metadataManager.getServiceMetadata(service).orElse(new ServiceMetadata());
            if (!serviceMetadata.getClusters().containsKey(clusterName)) {
                ClusterMetadata metadata = new ClusterMetadata();
                metadata.setHealthyCheckType(AbstractHealthChecker.None.TYPE);
                metadata.setHealthChecker(new AbstractHealthChecker.None());
                clusterOperator.updateClusterMetadata(Constants.DEFAULT_NAMESPACE_ID, serviceName,
                    clusterName, metadata);
            }
            String[] ipArray = addressServerManager.splitIps(ips);
            String checkResult = InternetAddressUtil.checkIps(ipArray);
            if (InternetAddressUtil.checkOk(checkResult)) {
                List<Instance> instanceList = addressServerGeneratorManager
                    .generateInstancesByIps(serviceName, productName, clusterName, ipArray);
                for (Instance instance : instanceList) {
                    instanceOperator.registerInstance(Constants.DEFAULT_NAMESPACE_ID, serviceName,
                        instance);
                }
            }
            return new Result(checkResult, ipArray.length);
        }
}
