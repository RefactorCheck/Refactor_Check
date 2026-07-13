public class nacos_0093 {

        public boolean saveConfigRefactored(CopilotProperties config) {
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
                    "nacos-copilot",
                    "system",
                    null,
                    "Copilot configuration",
                    "json");
                
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
