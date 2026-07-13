public class dubbo_0098 {

        private Map<String, String> resolveStatusCheckerNamesMapFromProtocolConfigs() {
    
            if (protocolConfigs.isEmpty()) {
                protocolConfigs = configManager.getConfigsMap(ProtocolConfig.class);
            }
    
            Map<String, String> statusCheckerNamesMap = new LinkedHashMap<>();
    
            for (Map.Entry<String, ProtocolConfig> entry : protocolConfigs.entrySet()) {
                addStatusCheckersToMap(entry.getKey(), entry.getValue(), statusCheckerNamesMap);
            }
    
            return statusCheckerNamesMap;
        }
    
        private void addStatusCheckersToMap(String beanName, ProtocolConfig protocolConfig, Map<String, String> statusCheckerNamesMap) {
            Set<String> statusCheckerNames = getStatusCheckerNames(protocolConfig);
            for (String statusCheckerName : statusCheckerNames) {
                String source = buildSource(beanName, protocolConfig);
                statusCheckerNamesMap.put(statusCheckerName, source);
            }
        }
}
