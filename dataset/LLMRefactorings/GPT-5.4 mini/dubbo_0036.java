public class dubbo_0036 {

        @Override
        public void doUnsubscribe(URL url, NotifyListener listener) {            doUnsubscribeExtracted(url, listener);
}

public class dubbo_0036 {

        @Override
        public void doUnsubscribeExtracted(URL url, NotifyListener listener) {
            if (isAdminProtocol(url)) {
                shutdownServiceNamesLookup();
            } else {
                Map<NotifyListener, NacosAggregateListener> listenerMap = originToAggregateListener.get(url);
                if (listenerMap == null) {
                    logger.warn(
                            REGISTRY_NACOS_EXCEPTION,
                            "",
                            "",
                            String.format(
                                    "No aggregate listener found for url %s, "
                                            + "this service might have already been unsubscribed.",
                                    url));
                    return;
                }
                NacosAggregateListener nacosAggregateListener = listenerMap.remove(listener);
                if (nacosAggregateListener != null) {
                    Set<String> serviceNames = nacosAggregateListener.getServiceNames();
                    try {
                        doUnsubscribe(url, nacosAggregateListener, serviceNames);
                    } catch (NacosException e) {
                        logger.error(
                                REGISTRY_NACOS_EXCEPTION,
                                "",
                                "",
                                "Failed to unsubscribe " + url + " to nacos " + getUrl() + ", cause: " + e.getMessage(),
                                e);
                    }
                }
                if (listenerMap.isEmpty()) {
                    originToAggregateListener.remove(url);
                }
            }
        
}
}
