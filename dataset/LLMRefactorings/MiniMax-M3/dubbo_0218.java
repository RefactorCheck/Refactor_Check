public class dubbo_0218 {

        private Set<String> filterServiceNames(NacosServiceName serviceName) {
            try {
                Set<String> serviceNames = new LinkedHashSet<>();
                serviceNames.addAll(getFilteredServiceNames(serviceName));
                return serviceNames;
            } catch (SkipFailbackWrapperException exception) {
                throw exception;
            } catch (Throwable cause) {
                throw new RpcException(
                        "Failed to filter serviceName from nacos, url: " + getUrl() + ", serviceName: " + serviceName
                                + ", cause: " + cause.getMessage(),
                        cause);
            }
        }

        private List<String> getFilteredServiceNames(NacosServiceName serviceName) {
            return namingService
                    .getServicesOfServer(1, Integer.MAX_VALUE, getUrl().getGroup(Constants.DEFAULT_GROUP))
                    .getData()
                    .stream()
                    .filter(this::isConformRules)
                    .map(NacosServiceName::new)
                    .filter(serviceName::isCompatible)
                    .map(NacosServiceName::toString)
                    .collect(Collectors.toList());
        }
}
