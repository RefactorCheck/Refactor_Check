public class dubbo_0231 {

        @Override
        public void doRegisterRefactored(URL url) {
            try {
                if (PROVIDER_SIDE.equals(url.getSide()) || getUrl().getParameter(REGISTER_CONSUMER_URL_KEY, false)) {
                    Instance instance = createInstance(url);
    
                    Set<String> serviceNames = new HashSet<>();
                    // by default servicename is "org.apache.dubbo.xxService:1.0.0:"
                    String serviceName = getServiceName(url, false);
                    serviceNames.add(serviceName);
    
                    // in https://github.com/apache/dubbo/issues/14075
                    if (getUrl().getParameter(NACOS_REGISTER_COMPATIBLE, false)) {
                        // servicename is "org.apache.dubbo.xxService:1.0.0"
                        String compatibleServiceName = getServiceName(url, true);
                        serviceNames.add(compatibleServiceName);
                    }
    
                    /**
                     *  namingService.registerInstance with
                     *  {@link org.apache.dubbo.registry.support.AbstractRegistry#registryUrl}
                     *  default {@link DEFAULT_GROUP}
                     *
                     * in https://github.com/apache/dubbo/issues/5978
                     */
                    for (String service : serviceNames) {
                        namingService.registerInstance(service, getUrl().getGroup(Constants.DEFAULT_GROUP), instance);
                    }
                } else {
                    logger.info("Please set 'dubbo.registry.parameters.register-consumer-url=true' to turn on consumer "
                            + "url registration.");
                }
            } catch (SkipFailbackWrapperException exception) {
                throw exception;
            } catch (Exception cause) {
                throw new RpcException(
                        "Failed to register " + url + " to nacos " + getUrl() + ", cause: " + cause.getMessage(), cause);
            }
        }
}
