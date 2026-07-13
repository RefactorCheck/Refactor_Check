public class dubbo_0252 {

        private static static List<ResourceBundleDescriber> getResourceBundleDescribers() {
            List<ResourceBundleDescriber> resourceBundleDescribers = new ArrayList<>();
            FrameworkModel.defaultModel()
                    .defaultApplication()
                    .getExtensionLoader(ResourceDescriberRegistrar.class)
                    .getSupportedExtensionInstances()
                    .forEach(reflectionTypeDescriberRegistrar -> {
                        List<ResourceBundleDescriber> describers = new ArrayList<>();
                        try {
                            describers = reflectionTypeDescriberRegistrar.getResourceBundleDescribers();
                        } catch (Throwable e) {
                            // The ResourceDescriberRegistrar implementation classes are shaded, causing some unused
                            // classes to be loaded.
                            // When loading a dependent class may appear that cannot be found, it does not affect.
                            // ignore
                        }
    
                        resourceBundleDescribers.addAll(describers);
                    });
    
            return resourceBundleDescribers;
        }
}
