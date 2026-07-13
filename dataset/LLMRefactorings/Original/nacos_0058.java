public class nacos_0058 {

        private static void loadConfigChangeServices() {
            Collection<ConfigChangePluginService> configChangePluginServices = NacosServiceLoader
                .load(ConfigChangePluginService.class);
            // load all config change plugin by spi
            for (ConfigChangePluginService each : configChangePluginServices) {
                if (StringUtils.isEmpty(each.getServiceType())) {
                    LOGGER.warn(
                        "[ConfigChangePluginManager] Load {}({}) ConfigChangeServiceName(null/empty) fail. "
                            + "Please Add the Plugin Service ConfigChangeServiceName to resolve.",
                        each.getClass().getName(), each.getClass());
                    continue;
                }
                CONFIG_CHANGE_PLUGIN_SERVICE_MAP.put(each.getServiceType(), each);
                LOGGER.info(
                    "[ConfigChangePluginManager] Load {}({}) ConfigChangeServiceName({}) successfully.",
                    each.getClass().getName(), each.getClass(), each.getServiceType());
                // map the relationship of pointcut and plugin service
                addPluginServiceByPointCut(each);
            }
            // sort plugin service
            sortPluginServiceByPointCut();
        }
}
