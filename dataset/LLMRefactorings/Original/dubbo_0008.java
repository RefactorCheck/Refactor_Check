public class dubbo_0008 {

        @Override
        public List<URL> build(ServiceInstance serviceInstance) {
            Map<String, String> paramsMap = getMetadataServiceURLsParams(serviceInstance);
    
            String serviceName = serviceInstance.getServiceName();
    
            String host = serviceInstance.getHost();
    
            URL url;
            if (paramsMap.isEmpty()) {
                // ServiceInstance Metadata is empty. Happened when registry not support metadata write.
                url = generateUrlWithoutMetadata(serviceName, host, serviceInstance.getPort());
            } else {
                url = generateWithMetadata(serviceName, host, paramsMap);
            }
    
            url = url.setScopeModel(serviceInstance.getApplicationModel().getInternalModule());
    
            return Collections.singletonList(url);
        }
}
