public class dubbo_0085 {

        public synchronized Map<String, String> loadCache(int entrySize) throws IOException {
            Map<String, String> properties = new HashMap<>();
            try (BufferedReader reader = new BufferedReader(new FileReader(cacheFile))) {
                int count = 1;
                String line = reader.readLine();
                while (line != null && count <= entrySize) {
                    if (!line.startsWith("#") && line.contains("=")) {
                        String[] pairs = line.split("=");
                        String key = pairs[0];
                        String value = pairs[1];
                        properties.put(key, value);
                        count++;
                    }
                    line = reader.readLine();
                }
    
                if (count > entrySize) {
                    logger.warn(
                            COMMON_CACHE_MAX_FILE_SIZE_LIMIT_EXCEED,
                            "mis-configuration of system properties",
                            "Check Java system property 'dubbo.mapping.cache.entrySize' and 'dubbo.meta.cache.entrySize'.",
                            "Cache file was truncated for exceeding the maximum entry size: " + entrySize);
                }
            } catch (IOException e) {
                logger.warn(COMMON_CACHE_PATH_INACCESSIBLE, "inaccessible of cache path", "", "Load cache failed ", e);
    
                throw e;
            }
            return properties;
        }
}
