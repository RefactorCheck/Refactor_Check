public class nacos_0005 {

        @Since("3.2.0")
        @PostMapping
        @Secured(action = ActionTypes.WRITE, signType = SignType.AI, apiType = ApiType.CONSOLE_API)
        public Result<Boolean> saveConfig(HttpServletRequest request,
            @RequestBody CopilotProperties config)
            throws NacosException {
            if (config == null) {
                throw new NacosException(NacosException.INVALID_PARAM, "Configuration cannot be null");
            }
            
            // Get existing config to preserve other fields, or create new one with defaults
            CopilotProperties existingConfig = getStoredConfig();
            CopilotProperties fullConfig;
            
            if (existingConfig != null) {
                // Use existing config and only update apiKey, model, studioUrl and studioProject
                fullConfig = existingConfig;
            } else {
                // Create new config with default values
                fullConfig = new CopilotProperties();
            }
            
            // Update only apiKey, model, studioUrl and studioProject
            if (config.getApiKey() != null) {
                fullConfig.setApiKey(config.getApiKey());
            }
            if (config.getModel() != null) {
                fullConfig.setModel(config.getModel());
            }
            if (config.getStudioUrl() != null) {
                fullConfig.setStudioUrl(config.getStudioUrl());
            }
            if (config.getStudioProject() != null) {
                fullConfig.setStudioProject(config.getStudioProject());
            }
            
            boolean success = publishStoredConfig(request, fullConfig);
            
            if (success) {
                // Refresh configuration after config update
                agentManager.refreshConfig();
            }
            
            return Result.success(success);
        }
}
