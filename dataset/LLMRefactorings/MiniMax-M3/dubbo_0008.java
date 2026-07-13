public class dubbo_0008 {

        @Override
        public List<URL> build(ServiceInstance serviceInstance) {
            Map<String, String> paramsMap = getMetadataServiceURLsParams(serviceInstance);

            String serviceName = serviceInstance.getServiceName();

            String host = serviceInstance.getHost();

            URL url = buildUrl(serviceName, host, paramsMap, serviceInstance.getPort());

            url = url.setScopeModel(serviceInstance.getApplicationModel().getInternalModule());

            return Collections.singletonList(url);
        }

        private URL buildUrl(String serviceName, String host, Map<String, String> paramsMap, int port) {
            if (paramsMap.isEmpty()) {
                return generateUrlWithoutMetadata(serviceName, host, port);
            }
            return generateWithMetadata(serviceName, host, paramsMap);
        }
}
