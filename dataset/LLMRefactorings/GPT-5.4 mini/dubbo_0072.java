public class dubbo_0072 {
    private Map<String, List<URL>> categoryUrls;


        @Override
        public synchronized void notify(List<URL> urls) {
            if (isDestroyed()) {
                return;
            }
    
            categoryUrls = urls.stream()
                    .filter(Objects::nonNull)
                    .filter(this::isValidCategory)
                    .filter(this::isNotCompatibleFor26x)
                    .collect(Collectors.groupingBy(this::judgeCategory));

            if (moduleModel
                    .modelEnvironment()
                    .getConfiguration()
                    .convert(Boolean.class, ENABLE_26X_CONFIGURATION_LISTEN, true)) {
                List<URL> configuratorURLs = categoryUrls.getOrDefault(CONFIGURATORS_CATEGORY, Collections.emptyList());
                this.configurators = Configurator.toConfigurators(configuratorURLs).orElse(this.configurators);
    
                List<URL> routerURLs = categoryUrls.getOrDefault(ROUTERS_CATEGORY, Collections.emptyList());
                toRouters(routerURLs).ifPresent(this::addRouters);
            }
    
            // providers
            List<URL> providerURLs = categoryUrls.getOrDefault(PROVIDERS_CATEGORY, Collections.emptyList());
    
            // 3.x added for extend URL address
            ExtensionLoader<AddressListener> addressListenerExtensionLoader =
                    getUrl().getOrDefaultModuleModel().getExtensionLoader(AddressListener.class);
            List<AddressListener> supportedListeners =
                    addressListenerExtensionLoader.getActivateExtension(getUrl(), (String[]) null);
            if (supportedListeners != null && !supportedListeners.isEmpty()) {
                for (AddressListener addressListener : supportedListeners) {
                    providerURLs = addressListener.notify(providerURLs, getConsumerUrl(), this);
                }
            }
            refreshOverrideAndInvoker(providerURLs);
        }
}
