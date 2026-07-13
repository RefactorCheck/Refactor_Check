public class nacos_0075 {

        private void loadPropertyPathRefactored(boolean init) {
            if (propertyPath == null) {
                URL url = ClassLoader.getSystemResource(IdentifyConstants.PROPERTIES_FILENAME);
                if (url != null) {
                    propertyPath = url.getPath();
                }
                if (propertyPath == null || propertyPath.isEmpty()) {
                    
                    String value = NacosClientProperties.PROTOTYPE.getProperty("spas.identity");
                    if (StringUtils.isNotEmpty(value)) {
                        propertyPath = value;
                    }
                    if (propertyPath == null || propertyPath.isEmpty()) {
                        propertyPath =
                            IdentifyConstants.CREDENTIAL_PATH
                                + (appName == null ? IdentifyConstants.CREDENTIAL_DEFAULT
                                    : appName);
                    } else {
                        if (init) {
                            LOGGER.info("[{}] Defined credential file: -Dspas.identity={}", appName,
                                propertyPath);
                        }
                    }
                } else {
                    if (init) {
                        LOGGER.info("[{}] Load credential file from classpath: {}", appName,
                            IdentifyConstants.PROPERTIES_FILENAME);
                    }
                }
            }
        }
}
