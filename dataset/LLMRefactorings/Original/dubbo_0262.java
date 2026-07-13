public class dubbo_0262 {

        @Override
        public void destroy() {
            if (isDestroyed()) {
                return;
            }
    
            // unregister.
            try {
                if (getRegisteredConsumerUrl() != null && registry != null && registry.isAvailable()) {
                    registry.unregister(getRegisteredConsumerUrl());
                }
            } catch (Throwable t) {
                // 1-8: Failed to unregister / unsubscribe url on destroy.
                logger.warn(
                        REGISTRY_FAILED_DESTROY_UNREGISTER_URL,
                        "",
                        "",
                        "unexpected error when unregister service " + serviceKey + " from registry: " + registry.getUrl(),
                        t);
            }
    
            // unsubscribe.
            try {
                if (getSubscribeUrl() != null && registry != null && registry.isAvailable()) {
                    registry.unsubscribe(getSubscribeUrl(), this);
                }
            } catch (Throwable t) {
                // 1-8: Failed to unregister / unsubscribe url on destroy.
                logger.warn(
                        REGISTRY_FAILED_DESTROY_UNREGISTER_URL,
                        "",
                        "",
                        "unexpected error when unsubscribe service " + serviceKey + " from registry: " + registry.getUrl(),
                        t);
            }
    
            ExtensionLoader<AddressListener> addressListenerExtensionLoader =
                    getUrl().getOrDefaultModuleModel().getExtensionLoader(AddressListener.class);
            List<AddressListener> supportedListeners =
                    addressListenerExtensionLoader.getActivateExtension(getUrl(), (String[]) null);
            if (CollectionUtils.isNotEmpty(supportedListeners)) {
                for (AddressListener addressListener : supportedListeners) {
                    addressListener.destroy(getConsumerUrl(), this);
                }
            }
    
            synchronized (this) {
                try {
                    destroyAllInvokers();
                } catch (Throwable t) {
                    // 1-15 - Failed to destroy service.
                    logger.warn(REGISTRY_FAILED_DESTROY_SERVICE, "", "", "Failed to destroy service " + serviceKey, t);
                }
                routerChain.destroy();
                invokersChangedListener = null;
                serviceListener = null;
    
                super.destroy(); // must be executed after unsubscribing
            }
        }
}
