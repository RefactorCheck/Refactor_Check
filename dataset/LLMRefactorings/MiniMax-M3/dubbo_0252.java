public class dubbo_0252 {

    private static List<ResourceBundleDescriber> getResourceBundleDescribers() {
        List<ResourceBundleDescriber> resourceBundleDescribers = new ArrayList<>();
        FrameworkModel.defaultModel()
                .defaultApplication()
                .getExtensionLoader(ResourceDescriberRegistrar.class)
                .getSupportedExtensionInstances()
                .forEach(reflectionTypeDescriberRegistrar ->
                        resourceBundleDescribers.addAll(safeGetResourceBundleDescribers(reflectionTypeDescriberRegistrar)));
        return resourceBundleDescribers;
    }

    private static List<ResourceBundleDescriber> safeGetResourceBundleDescribers(ResourceDescriberRegistrar registrar) {
        try {
            return registrar.getResourceBundleDescribers();
        } catch (Throwable e) {
            return new ArrayList<>();
        }
    }
}
