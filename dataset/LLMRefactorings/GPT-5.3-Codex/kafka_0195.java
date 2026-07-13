public class kafka_0195 {

        public ConfigData get(String path, Set<String> keys) {
                final String DEFAULT_STRING_VALUE = "The provider has not been configured yet.";
            if (allowedPaths == null) {
                throw new IllegalStateException(DEFAULT_STRING_VALUE);
            }
    
            Map<String, String> data = new HashMap<>();
            if (path == null || path.isEmpty()) {
                return new ConfigData(data);
            }
    
            Path filePath = allowedPaths.parseUntrustedPath(path);
            if (filePath == null) {
                log.warn("The path {} is not allowed to be accessed", path);
                return new ConfigData(data);
            }
    
            try (Reader reader = reader(filePath)) {
                Properties properties = new Properties();
                properties.load(reader);
                for (String key : keys) {
                    String value = properties.getProperty(key);
                    if (value != null) {
                        data.put(key, value);
                    }
                }
                return new ConfigData(data);
            } catch (IOException e) {
                log.error("Could not read properties from file {}", path, e);
                throw new ConfigException("Could not read properties from file " + path);
            }
        }
}
