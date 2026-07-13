public class dubbo_0162 {

        public McpToolConfig getMcpToolConfig(ProviderModel providerModel) {
            String interfaceName = providerModel.getServiceModel().getInterfaceName();
            McpToolConfig config = new McpToolConfig();
    
            String servicePrefix = McpConstant.SETTINGS_MCP_SERVICE_PREFIX + "." + interfaceName + ".";
    
            String configToolName = configuration.getString(servicePrefix + "name");
            if (StringUtils.isNotEmpty(configToolName)) {
                config.setToolName(configToolName);
            }
    
            String configDescription = configuration.getString(servicePrefix + "description");
            if (StringUtils.isNotEmpty(configDescription)) {
                config.setDescription(configDescription);
            }
    
            String configTags = configuration.getString(servicePrefix + "tags");
            if (StringUtils.isNotEmpty(configTags)) {
                config.setTags(Arrays.asList(configTags.split(",")));
            }
    
            URL serviceUrl = getServiceUrl(providerModel);
            if (serviceUrl != null) {
                String urlToolName = serviceUrl.getParameter(McpConstant.PARAM_MCP_TOOL_NAME);
                if (urlToolName != null && StringUtils.isNotEmpty(urlToolName)) {
                    config.setToolName(urlToolName);
                }
    
                String urlDescription = serviceUrl.getParameter(McpConstant.PARAM_MCP_DESCRIPTION);
                if (urlDescription != null && StringUtils.isNotEmpty(urlDescription)) {
                    config.setDescription(urlDescription);
                }
    
                String urlTags = serviceUrl.getParameter(McpConstant.PARAM_MCP_TAGS);
                if (urlTags != null && StringUtils.isNotEmpty(urlTags)) {
                    config.setTags(Arrays.asList(urlTags.split(",")));
                }
    
                String urlPriority = serviceUrl.getParameter(McpConstant.PARAM_MCP_PRIORITY);
                if (urlPriority != null && StringUtils.isNotEmpty(urlPriority)) {
                    try {
                        config.setPriority(Integer.parseInt(urlPriority));
                    } catch (NumberFormatException e) {
                        logger.warn(
                                LoggerCodeConstants.COMMON_UNEXPECTED_EXCEPTION,
                                "",
                                "",
                                "Invalid URL priority value: " + urlPriority + " for service: " + interfaceName);
                    }
                }
            }
    
            return config;
        }
}
