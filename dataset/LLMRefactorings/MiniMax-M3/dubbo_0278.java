public class dubbo_0278 {

        private Map<String, String> getConfigurations(String key, Environment environment) {
            Object rawProperties = environment.getProperty(key, Object.class);
            Map<String, String> externalProperties = new HashMap<>();
            try {
                if (rawProperties instanceof Map) {
                    externalProperties.putAll((Map<String, String>) rawProperties);
                } else if (rawProperties instanceof String) {
                    externalProperties.putAll(ConfigurationUtils.parseProperties((String) rawProperties));
                }
    
                if (environment instanceof ConfigurableEnvironment && externalProperties.isEmpty()) {
                    populateFromPropertySource(key, (ConfigurableEnvironment) environment, externalProperties);
                }
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
            return externalProperties;
        }

        private void populateFromPropertySource(String key, ConfigurableEnvironment configurableEnvironment, Map<String, String> externalProperties) {
            PropertySource propertySource =
                    configurableEnvironment.getPropertySources().get(key);
            if (propertySource != null) {
                Object source = propertySource.getSource();
                if (source instanceof Map) {
                    ((Map<String, Object>) source).forEach((k, v) -> {
                        externalProperties.put(k, (String) v);
                    });
                }
            }
        }
}
