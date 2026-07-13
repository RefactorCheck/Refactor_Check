public class arthas_0275 {

        public Map<String, String> readConfigInfo (String filePath) {
            Map<String, String> nativeAgentDiscoveryConfigMap = new ConcurrentHashMap<>();
            ClassLoader classLoader = NativeAgentDiscoveryFactory.class.getClassLoader();
    
            try (InputStream inputStream = classLoader.getResourceAsStream(filePath);
                 BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
    
                if (inputStream == null) {
                    throw new IllegalArgumentException("File not found: " + filePath);
                }
    
                String line;
                while ((line = reader.readLine()) != null) {
                    parseConfigLine(line, nativeAgentDiscoveryConfigMap);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return nativeAgentDiscoveryConfigMap;
        }

        private void parseConfigLine(String line, Map<String, String> configMap) {
            if (!line.trim().isEmpty() && line.contains("=")) {
                String[] parts = line.split("=", 2);
                if (parts.length == 2) {
                    configMap.put(parts[0].trim(), parts[1].trim());
                }
            }
        }
}
