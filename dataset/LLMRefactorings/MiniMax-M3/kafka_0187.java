public class kafka_0187 {

    private static final String EMPTY_STRING = "";

    private static ConfigInfos prefixedConfigInfos(Map<String, ConfigKey> configKeys, List<ConfigValue> configValues, String prefix) {
        int errorCount = 0;
        Set<String> groups = new LinkedHashSet<>();
        List<ConfigInfo> configInfos = new ArrayList<>();

        if (configValues == null) {
            return new ConfigInfos(EMPTY_STRING, 0, new ArrayList<>(groups), configInfos);
        }

        for (ConfigValue validatedConfigValue : configValues) {
            ConfigKey configKey = configKeys.get(validatedConfigValue.name());
            ConfigKeyInfo configKeyInfo = null;
            if (configKey != null) {
                if (configKey.group != null) {
                    groups.add(configKey.group);
                }
                configKeyInfo = convertConfigKey(configKey, prefix);
            }

            ConfigValue configValue = new ConfigValue(prefix + validatedConfigValue.name(), validatedConfigValue.value(),
                    validatedConfigValue.recommendedValues(), validatedConfigValue.errorMessages());
            if (configValue.errorMessages().size() > 0) {
                errorCount++;
            }
            ConfigValueInfo configValueInfo = convertConfigValue(configValue, configKey != null ? configKey.type : null);
            configInfos.add(new ConfigInfo(configKeyInfo, configValueInfo));
        }
        return new ConfigInfos(EMPTY_STRING, errorCount, new ArrayList<>(groups), configInfos);
    }
}
