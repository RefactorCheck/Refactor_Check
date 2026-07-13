public class dubbo_0158 {

        private static void register(Registry registry, URL registeredProviderUrl) {
            ApplicationDeployer deployer =
                    registeredProviderUrl.getOrDefaultApplicationModel().getDeployer();
            try {
                deployer.increaseServiceRefreshCount();
                String registryName = resolveRegistryName(registry.getUrl());
                MetricsEventBus.post(
                        RegistryEvent.toRsEvent(
                                registeredProviderUrl.getApplicationModel(),
                                registeredProviderUrl.getServiceKey(),
                                1,
                                Collections.singletonList(registryName)),
                        () -> {
                            registry.register(registeredProviderUrl);
                            return null;
                        });
            } finally {
                deployer.decreaseServiceRefreshCount();
            }
        }

        private static String resolveRegistryName(URL url) {
            return Optional.ofNullable(url)
                    .map(u -> u.getParameter(
                            RegistryConstants.REGISTRY_CLUSTER_KEY,
                            UrlUtils.isServiceDiscoveryURL(u) ? u.getParameter(REGISTRY_KEY) : u.getProtocol()))
                    .filter(StringUtils::isNotEmpty)
                    .orElse("unknown");
        }
}
