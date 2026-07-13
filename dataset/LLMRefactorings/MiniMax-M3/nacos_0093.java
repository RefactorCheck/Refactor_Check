public class nacos_0093 {

    private static final String CONFIG_APP_NAME = "nacos-copilot";
    private static final String CONFIG_SOURCE = "system";
    private static final String CONFIG_DESCRIPTION = "Copilot configuration";
    private static final String CONFIG_TYPE = "json";

        public boolean saveConfig(CopilotProperties config) {
            if (configMaintainerService == null) {
                LOGGER.warn("Config maintainer service is not initialized, cannot save config");
                return false;
            }
            
            String content = JacksonUtils.toJson(config);
            try {
                boolean result = configMaintainerService.publishConfig(
                    CONFIG_DATA_ID,
                    CONFIG_GROUP,
                    configNamespace,
                    content,
                    CONFIG_APP_NAME,
                    CONFIG_SOURCE,
                    null,
                    CONFIG_DESCRIPTION,
                    CONFIG_TYPE);
                
                if (result || isTargetConfigSaved(content)) {
                    LOGGER.info("Copilot config saved successfully to Nacos Config");
                    return true;
                } else {
                    LOGGER.warn("Failed to save Copilot config to Nacos Config");
                }
                
                return false;
            } catch (NacosException e) {
                if (isTargetConfigSaved(content)) {
                    LOGGER.info("Copilot config already saved to Nacos Config");
                    return true;
                }
                LOGGER.error("Failed to save Copilot config to Nacos Config", e);
                return false;
            }
        }
}
