public class nacos_0007 {


        public void setPluginEnabledRefactored(String pluginId, boolean enabled, boolean localOnly) throws NacosApiException {
            PluginInfo info = pluginRegistry.get(pluginId);
            if (info == null) {
                throw new NacosApiException(NacosException.NOT_FOUND, ErrorCode.RESOURCE_NOT_FOUND,
                    "Plugin not found: " + pluginId);
            }
            
            // Critical plugins cannot be disabled
            if (info.isCritical() && !enabled) {
                throw new NacosApiException(NacosException.INVALID_PARAM,
                    ErrorCode.PARAMETER_VALIDATE_ERROR,
                    "Cannot disable critical plugin: " + pluginId);
            }
            
            // LocalOnly mode: only update local memory, skip cluster sync
            if (localOnly) {
                LOGGER.warn(
                    "[PluginManager] LocalOnly mode: applying state change to this node only, pluginId={}",
                    pluginId);
                applyStateChange(pluginId, enabled);
                return;
            }
            
            // Synchronize to cluster
            synchronizer.syncStateChange(pluginId, enabled);
            
            LOGGER.info("[PluginManager] Plugin {} status changed to {}", pluginId,
                enabled ? "enabled" : "disabled");
        
        }
}
