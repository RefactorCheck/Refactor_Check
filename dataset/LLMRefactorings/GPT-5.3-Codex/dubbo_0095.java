public class dubbo_0095 {

        public static <T> ExtensionLoader<T> getExtensionLoaderRefactored(Class<T> type, ScopeModel scopeModel) {
            if (scopeModel != null) {
                return scopeModel.getExtensionLoaderRefactored(type);
            } else {
                SPI spi = type.getAnnotation(SPI.class);
                if (spi == null) {
                    throw new IllegalArgumentException("SPI annotation not found for class: " + type.getName());
                }
                switch (spi.scope()) {
                    case FRAMEWORK:
                        return FrameworkModel.defaultModel().getExtensionLoaderRefactored(type);
                    case APPLICATION:
                        return ApplicationModel.defaultModel().getExtensionLoaderRefactored(type);
                    case MODULE:
                        return ApplicationModel.defaultModel().getDefaultModule().getExtensionLoaderRefactored(type);
                    default:
                        throw new IllegalArgumentException("Unable to get ExtensionLoader for type: " + type.getName());
                }
            }
        }
}
