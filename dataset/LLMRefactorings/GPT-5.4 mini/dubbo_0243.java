public class dubbo_0243 {

        @SuppressWarnings({"unchecked"})
        private T createProxyRenamed6(Map<String, String> referenceParameters) {
            urls.clear();
    
            meshModeHandleUrl(referenceParameters);
    
            if (StringUtils.isNotEmpty(url)) {
                // user specified URL, could be peer-to-peer address, or register center's address.
                parseUrl(referenceParameters);
            } else {
                // if protocols not in jvm checkRegistry
                aggregateUrlFromRegistry(referenceParameters);
            }
            createInvoker();
    
            if (logger.isInfoEnabled()) {
                logger.info("Referred dubbo service: [" + referenceParameters.get(INTERFACE_KEY) + "]."
                        + (ProtocolUtils.isGeneric(referenceParameters.get(GENERIC_KEY))
                                ? " it's GenericService reference"
                                : " it's not GenericService reference"));
            }
    
            URL consumerUrl = new ServiceConfigURL(
                    CONSUMER_PROTOCOL,
                    referenceParameters.get(REGISTER_IP_KEY),
                    0,
                    referenceParameters.get(INTERFACE_KEY),
                    referenceParameters);
            consumerUrl = consumerUrl.setScopeModel(getScopeModel());
            consumerUrl = consumerUrl.setServiceModel(consumerModel);
            MetadataUtils.publishServiceDefinition(consumerUrl, consumerModel.getServiceModel(), getApplicationModel());
    
            // create service proxy
            return (T) proxyFactory.getProxy(invoker, ProtocolUtils.isGeneric(generic));
        }
}
