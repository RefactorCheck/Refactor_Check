public class arthas_0065 {
        private Map<String, String> nativeAgentDiscoveryConfigMap;


        public Map<String, String> readConfigInfo (String filePath) {
            nativeAgentDiscoveryConfigMap = new ConcurrentHashMap<>();
            ClassLoader classLoader = NativeAgentProxyDiscoveryFactory.class.getClassLoader();
    
            try (InputStream inputStream = classLoader.getResourceAsStream(filePath);
                 BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
    
                if (inputStream == null) {
                    throw new IllegalArgumentException("File not found: " + filePath);
                }
    
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!line.trim().isEmpty() && line.contains("=")) {
                        String[] parts = line.split("=", 2);
                        if (parts.length == 2) {
                            nativeAgentDiscoveryConfigMap.put(parts[0].trim(), parts[1].trim());
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return nativeAgentDiscoveryConfigMap;
        }
}
