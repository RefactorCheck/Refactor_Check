public class dubbo_0274 {

    @Override
    public String execute(CommandContext commandContext, String[] args) {
        String config = collectStartupProbeConfig();

        URL url = URL.valueOf("application://").addParameter(CommonConstants.QOS_STARTUP_PROBE_EXTENSION, config);
        List<StartupProbe> startupProbes = frameworkModel
                .getExtensionLoader(StartupProbe.class)
                .getActivateExtension(url, CommonConstants.QOS_STARTUP_PROBE_EXTENSION);
        if (!startupProbes.isEmpty()) {
            for (StartupProbe startupProbe : startupProbes) {
                if (!startupProbe.check()) {
                    // 503 Service Unavailable
                    commandContext.setHttpCode(503);
                    return "false";
                }
            }
        }
        // 200 OK
        commandContext.setHttpCode(200);
        return "true";
    }

    private String collectStartupProbeConfig() {
        return frameworkModel.getApplicationModels().stream()
                .map(applicationModel ->
                        applicationModel.getApplicationConfigManager().getApplication())
                .map(o -> o.orElse(null))
                .filter(Objects::nonNull)
                .map(ApplicationConfig::getStartupProbe)
                .filter(Objects::nonNull)
                .collect(Collectors.joining(","));
    }
}
