public class dubbo_0114 {

        private void destroyUnusedInvokers(Map<URL, Invoker<T>> oldUrlInvokerMap, Map<URL, Invoker<T>> newUrlInvokerMap) {
            if (CollectionUtils.isEmptyMap(newUrlInvokerMap)) {
                destroyAllInvokers();
                return;
            }
    
            if (CollectionUtils.isEmptyMap(oldUrlInvokerMap)) {
                return;
            }
    
            for (Map.Entry<URL, Invoker<T>> entry : oldUrlInvokerMap.entrySet()) {
                destroyInvoker(entry.getValue());
            }
    
            logger.info(
                    "New url total size, " + newUrlInvokerMap.size() + ", destroyed total size " + oldUrlInvokerMap.size());
        }
    
        private void destroyInvoker(Invoker<T> invoker) {
            if (invoker != null) {
                try {
                    invoker.destroy();
                    if (logger.isDebugEnabled()) {
                        logger.debug("destroy invoker[" + invoker.getUrl() + "] success. ");
                    }
                } catch (Exception e) {
                    logger.warn(
                            REGISTRY_FAILED_DESTROY_SERVICE,
                            "",
                            "",
                            "destroy invoker[" + invoker.getUrl() + "] failed. " + e.getMessage(),
                            e);
                }
            }
        }
}
