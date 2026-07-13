public class nacos_0058 {

    private static void loadConfigChangeServices() {
        Collection<ConfigChangePluginService> configChangePluginServices = NacosServiceLoader
            .load(ConfigChangePluginService.class);
        for (ConfigChangePluginService each : configChangePluginServices) {
            registerConfigChangeService(each);
        }
        sortPluginServiceByPointCut();
    }

    private static void registerConfigChangeService(ConfigChangePluginService configChangePluginService) {
        if (StringUtils.isEmpty(configChangePluginService.getServiceType())) {
            LOGGER.warn(
                "[ConfigChangePluginManager] Load {}({}) ConfigChangeServiceName(null/empty) fail. "
                    + "Please Add the Plugin Service ConfigChangeServiceName to resolve.",
                configChangePluginService.getClass().getName(), configChangePluginService.getClass());
            return;
        }
        CONFIG_CHANGE_PLUGIN_SERVICE_MAP.put(configChangePluginService.getServiceType(), configChangePluginService);
        LOGGER.info(
            "[ConfigChangePluginManager] Load {}({}) ConfigChangeServiceName({}) successfully.",
            configChangePluginService.getClass().getName(), configChangePluginService.getClass(), configChangePluginService.getServiceType());
        addPluginServiceByPointCut(configChangePluginService);
    }
}
