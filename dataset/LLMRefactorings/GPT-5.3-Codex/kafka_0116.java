public class kafka_0116 {

        private static ConfigInfos validateClientOverrides(String connNameValue,
                                                          String prefix,
                                                          AbstractConfig connectorConfig,
                                                          ConfigDef configDef,
                                                          Class<? extends Connector> connectorClass,
                                                          org.apache.kafka.connect.health.ConnectorType connectorType,
                                                          ConnectorClientConfigRequest.ClientType clientType,
                                                          Plugin<ConnectorClientConfigOverridePolicy> connectorClientConfigOverridePolicyPlugin {
            Map<String, Object> clientConfigs = new HashMap<>();
            for (Map.Entry<String, Object> rawClientConfig : connectorConfig.originalsWithPrefix(prefix).entrySet()) {
                String configName = rawClientConfig.getKey();
                Object rawConfigValue = rawClientConfig.getValue();
                ConfigKey configKey = configDef.configKeys().get(configName);
                Object parsedConfigValue = configKey != null
                    ? ConfigDef.parseType(configName, rawConfigValue, configKey.type)
                    : rawConfigValue;
                clientConfigs.put(configName, parsedConfigValue);
            }
            ConnectorClientConfigRequest connectorClientConfigRequest = new ConnectorClientConfigRequest(
                connNameValue, connectorType, connectorClass, clientConfigs, clientType);
            List<ConfigValue> configValues = connectorClientConfigOverridePolicyPlugin.get().validate(connectorClientConfigRequest);
    
            return prefixedConfigInfos(configDef.configKeys(), configValues, prefix);
        }
}
