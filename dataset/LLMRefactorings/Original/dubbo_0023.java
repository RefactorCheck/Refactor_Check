public class dubbo_0023 {

        private void loadFromClass(
                Map<String, Class<?>> extensionClasses,
                boolean overridden,
                Set<java.net.URL> urls,
                ClassLoader classLoader,
                String[] includedPackages,
                String[] excludedPackages,
                String[] onlyExtensionClassLoaderPackages) {
            if (CollectionUtils.isNotEmpty(urls)) {
                for (java.net.URL url : urls) {
                    loadResource(
                            extensionClasses,
                            classLoader,
                            url,
                            overridden,
                            includedPackages,
                            excludedPackages,
                            onlyExtensionClassLoaderPackages);
                }
            }
        }
}
