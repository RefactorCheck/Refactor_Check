public class dubbo_0231 {

        @Override
        public void doRegister(URL url) {
            try {
                if (PROVIDER_SIDE.equals(url.getSide()) || getUrl().getParameter(REGISTER_CONSUMER_URL_KEY, false)) {
                    registerServiceInstances(url);
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

        private void registerServiceInstances(URL url) {
            Instance instance = createInstance(url);

            Set<String> serviceNames = new HashSet<>();
            String serviceName = getServiceName(url, false);
            serviceNames.add(serviceName);

            if (getUrl().getParameter(NACOS_REGISTER_COMPATIBLE, false)) {
                String compatibleServiceName = getServiceName(url, true);
                serviceNames.add(compatibleServiceName);
            }

            for (String service : serviceNames) {
                namingService.registerInstance(service, getUrl().getGroup(Constants.DEFAULT_GROUP), instance);
            }
        }
}
