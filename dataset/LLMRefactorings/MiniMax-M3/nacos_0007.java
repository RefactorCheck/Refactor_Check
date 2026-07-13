public class nacos_0007 {

    public void setPluginEnabled(String pluginId, boolean enabled, boolean localOnly)
        throws NacosApiException {
        PluginInfo info = pluginRegistry.get(pluginId);
        if (info == null) {
            throw new NacosApiException(NacosException.NOT_FOUND, ErrorCode.RESOURCE_NOT_FOUND,
                "Plugin not found: " + pluginId);
        }

        validateCriticalPlugin(info, pluginId, enabled);

        if (localOnly) {
            LOGGER.warn(
                "[PluginManager] LocalOnly mode: applying state change to this node only, pluginId={}",
                pluginId);
            applyStateChange(pluginId, enabled);
            return;
        }

        synchronizer.syncStateChange(pluginId, enabled);

        LOGGER.info("[PluginManager] Plugin {} status changed to {}", pluginId,
            enabled ? "enabled" : "disabled");
    }

    private void validateCriticalPlugin(PluginInfo info, String pluginId, boolean enabled)
        throws NacosApiException {
        if (info.isCritical() && !enabled) {
            throw new NacosApiException(NacosException.INVALID_PARAM,
                ErrorCode.PARAMETER_VALIDATE_ERROR,
                "Cannot disable critical plugin: " + pluginId);
        }
    }
}
