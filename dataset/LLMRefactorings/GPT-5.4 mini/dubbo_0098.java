public static class dubbo_0098 {

        private Map<String, String> resolveStatusCheckerNamesMapFromProtocolConfigs() {
    
            if (protocolConfigs.isEmpty()) {
                protocolConfigs = configManager.getConfigsMap(ProtocolConfig.class);
            }
    
            Map<String, String> statusCheckerNamesMap = new LinkedHashMap<>();
    
            for (Map.Entry<String, ProtocolConfig> entry : protocolConfigs.entrySet()) {
    
                String beanName = entry.getKey();
    
                ProtocolConfig protocolConfig = entry.getValue();
    
                Set<String> statusCheckerNames = getStatusCheckerNames(protocolConfig);
    
                for (String statusCheckerName : statusCheckerNames) {
    
                    String source = buildSource(beanName, protocolConfig);
    
                    statusCheckerNamesMap.put(statusCheckerName, source);
                }
            }
    
            return statusCheckerNamesMap;
        }
}
