public class nacos_0159 {

        public void updatePluginConfig(String pluginId, Map<String, String> config, boolean localOnly)
            throws NacosApiException {
            PluginInfo info = pluginRegistry.get(pluginId);
            if (info == null) {
                throw new NacosApiException(NacosException.NOT_FOUND, ErrorCode.RESOURCE_NOT_FOUND,
                    "Plugin not found: " + pluginId);
            }
            
            if (!info.isConfigurable()) {
                throw new NacosApiException(NacosException.INVALID_PARAM,
                    ErrorCode.PARAMETER_VALIDATE_ERROR,
                    "Plugin does not support configuration: " + pluginId);
            }
            
            // Validate config
            validateConfig(info, config);
            
            // LocalOnly mode: only update local memory, skip cluster sync
            if (localOnly) {
                LOGGER.warn(
                    "[PluginManager] LocalOnly mode: applying config change to this node only, pluginId={}",
                    pluginId);
                applyConfigChange(pluginId, config);
                return;
            }
            
            // Synchronize to cluster
            synchronizer.syncConfigChange(pluginId, config);
            
            LOGGER.info("[PluginManager] Plugin {} config updated", pluginId);
        }
}
