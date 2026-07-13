public class nacos_0016 {

    private void loadInitial() {
        Collection<CustomEnvironmentPluginService> customEnvironmentPluginServices =
            NacosServiceLoader.load(
                CustomEnvironmentPluginService.class);
        logLoadedServices(customEnvironmentPluginServices);
        SERVICE_LIST.addAll(customEnvironmentPluginServices.stream()
            .sorted(Comparator.comparingInt(CustomEnvironmentPluginService::order))
            .collect(Collectors.toList()));
    }

    private void logLoadedServices(Collection<CustomEnvironmentPluginService> customEnvironmentPluginServices) {
        for (CustomEnvironmentPluginService customEnvironmentPluginService : customEnvironmentPluginServices) {
            if (StringUtils.isBlank(customEnvironmentPluginService.pluginName())) {
                LOGGER.warn(
                    "[customEnvironmentPluginService] Load customEnvironmentPluginService({}) customEnvironmentPluginName(null/empty) fail."
                        + " Please Add customEnvironmentPluginName to resolve.",
                    customEnvironmentPluginService.getClass());
                continue;
            }
            LOGGER.info(
                "[CustomEnvironmentPluginManager] Load customEnvironmentPluginService({}) customEnvironmentPluginName({}) successfully.",
                customEnvironmentPluginService.getClass(),
                customEnvironmentPluginService.pluginName());
        }
    }
}
