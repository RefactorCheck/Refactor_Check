public class dubbo_0095 {

    public static <T> ExtensionLoader<T> getExtensionLoader(Class<T> type, ScopeModel scopeModel) {
        if (scopeModel != null) {
            return scopeModel.getExtensionLoader(type);
        } else {
            return getExtensionLoaderBySPI(type);
        }
    }

    private static <T> ExtensionLoader<T> getExtensionLoaderBySPI(Class<T> type) {
        SPI spi = type.getAnnotation(SPI.class);
        if (spi == null) {
            throw new IllegalArgumentException("SPI annotation not found for class: " + type.getName());
        }
        switch (spi.scope()) {
            case FRAMEWORK:
                return FrameworkModel.defaultModel().getExtensionLoader(type);
            case APPLICATION:
                return ApplicationModel.defaultModel().getExtensionLoader(type);
            case MODULE:
                return ApplicationModel.defaultModel().getDefaultModule().getExtensionLoader(type);
            default:
                throw new IllegalArgumentException("Unable to get ExtensionLoader for type: " + type.getName());
        }
    }
}
