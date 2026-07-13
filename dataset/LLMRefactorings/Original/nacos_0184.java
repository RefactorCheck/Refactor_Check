public class nacos_0184 {

        private ServiceInfo tryToSubscribe(String serviceName, String groupName,
            ServiceInfo cachedServiceInfo)
            throws NacosException {
            // not found in cache, service never subscribed.
            if (null == cachedServiceInfo) {
                return clientProxy.subscribe(serviceName, groupName, StringUtils.EMPTY);
            }
            // found in cache, and subscribed.
            if (clientProxy.isSubscribed(serviceName, groupName, StringUtils.EMPTY)) {
                return cachedServiceInfo;
            }
            // found in cached, but not subscribed, such as cached from local file when starting.
            ServiceInfo result = cachedServiceInfo;
            try {
                result = clientProxy.subscribe(serviceName, groupName, StringUtils.EMPTY);
            } catch (NacosException e) {
                NAMING_LOGGER.warn("Subscribe from Server failed, will use local cache. fail message: ",
                    e);
            }
            return result;
        }
}
