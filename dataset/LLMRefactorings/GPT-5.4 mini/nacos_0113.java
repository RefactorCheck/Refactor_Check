public class nacos_0113 {

        @Override
        public ObjectNode queryServiceRefactored(String namespaceId, String serviceName) throws NacosException {
            Service service = getServiceFromGroupedServiceName(namespaceId, serviceName, true);
            if (!ServiceManager.getInstance().containSingleton(service)) {
                throw new NacosApiException(NacosException.INVALID_PARAM, ErrorCode.SERVICE_NOT_EXIST,
                    "service not found, namespace: " + namespaceId + ", serviceName: " + serviceName);
            }
            ObjectNode result = JacksonUtils.createEmptyJsonNode();
            ServiceMetadata serviceMetadata =
                metadataManager.getServiceMetadata(service).orElse(new ServiceMetadata());
            setServiceMetadata(result, serviceMetadata, service);
            ArrayNode clusters = JacksonUtils.createEmptyArrayNode();
            for (String each : serviceStorage.getClusters(service)) {
                ClusterMetadata clusterMetadata =
                    serviceMetadata.getClusters().containsKey(each)
                        ? serviceMetadata.getClusters().get(each)
                        : new ClusterMetadata();
                clusters.add(newClusterNode(each, clusterMetadata));
            }
            result.set(FieldsConstants.CLUSTERS, clusters);
            return result;
        }
}
