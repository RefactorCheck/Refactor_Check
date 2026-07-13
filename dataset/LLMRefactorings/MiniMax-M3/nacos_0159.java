public class nacos_0159 {

        private static final String PLUGIN_NOT_FOUND_MESSAGE = "Plugin not found: ";
        private static final String PLUGIN_NOT_CONFIGURABLE_MESSAGE = "Plugin does not support configuration: ";
        private static final String LOCAL_ONLY_LOG_FORMAT = "[PluginManager] LocalOnly mode: applying config change to this node only, pluginId={}";
        private static final String CONFIG_UPDATED_LOG_FORMAT = "[PluginManager] Plugin {} config updated";

        public void updatePluginConfig(String pluginId, Map<String, String> config, boolean localOnly)
            throws NacosApiException {
            PluginInfo info = pluginRegistry.get(pluginId);
            if (info == null) {
                throw new NacosApiException(NacosException.NOT_FOUND, ErrorCode.RESOURCE_NOT_FOUND,
                    PLUGIN_NOT_FOUND_MESSAGE + pluginId);
            }

            if (!info.isConfigurable()) {
                throw new NacosApiException(NacosException.INVALID_PARAM,
                    ErrorCode.PARAMETER_VALIDATE_ERROR,
                    PLUGIN_NOT_CONFIGURABLE_MESSAGE + pluginId);
            }

            // Validate config
            validateConfig(info, config);

            // LocalOnly mode: only update local memory, skip cluster sync
            if (localOnly) {
                LOGGER.warn(LOCAL_ONLY_LOG_FORMAT, pluginId);
                applyConfigChange(pluginId, config);
                return;
            }

            // Synchronize to cluster
            synchronizer.syncConfigChange(pluginId, config);

            LOGGER.info(CONFIG_UPDATED_LOG_FORMAT, pluginId);
        }
}
