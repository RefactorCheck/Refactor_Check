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

            CopilotProperties fullConfig = mergeStoredConfig(config);

            boolean success = publishStoredConfig(request, fullConfig);

            if (success) {
                agentManager.refreshConfig();
            }

            return Result.success(success);
        }

        private CopilotProperties mergeStoredConfig(CopilotProperties config) {
            CopilotProperties existingConfig = getStoredConfig();
            CopilotProperties fullConfig = existingConfig != null ? existingConfig : new CopilotProperties();

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

            return fullConfig;
        }
}
