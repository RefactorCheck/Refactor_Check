public class kafka_0195 {

        public ConfigData get(String path, Set<String> keys) {
            if (allowedPaths == null) {
                throw new IllegalStateException("The provider has not been configured yet.");
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
                return readProperties(reader, keys, data);
            } catch (IOException e) {
                log.error("Could not read properties from file {}", path, e);
                throw new ConfigException("Could not read properties from file " + path);
            }
        }

        private ConfigData readProperties(Reader reader, Set<String> keys, Map<String, String> data) throws IOException {
            Properties properties = new Properties();
            properties.load(reader);
            for (String key : keys) {
                String value = properties.getProperty(key);
                if (value != null) {
                    data.put(key, value);
                }
            }
            return new ConfigData(data);
        }
}
