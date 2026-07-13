public class dubbo_0204 {

    private static List<URL> genCompatibleRegistries(ScopeModel scopeModel, List<URL> registryList, boolean provider) {
        List<URL> result = new ArrayList<>(registryList.size());
        registryList.forEach(registryURL -> {
            if (provider) {
                processProviderRegistry(registryURL, scopeModel, registryList, result);
            } else {
                result.add(registryURL);
            }
        });
        return result;
    }

    private void processProviderRegistry(URL registryURL, ScopeModel scopeModel, List<URL> registryList, List<URL> result) {
        String registerMode;
        if (SERVICE_REGISTRY_PROTOCOL.equals(registryURL.getProtocol())) {
            registerMode = registryURL.getParameter(
                    REGISTER_MODE_KEY,
                    ConfigurationUtils.getCachedDynamicProperty(
                            scopeModel, DUBBO_REGISTER_MODE_DEFAULT_KEY, DEFAULT_REGISTER_MODE_INSTANCE));
            if (!isValidRegisterMode(registerMode)) {
                registerMode = DEFAULT_REGISTER_MODE_INSTANCE;
            }
            result.add(registryURL);
            if (DEFAULT_REGISTER_MODE_ALL.equalsIgnoreCase(registerMode)
                    && registryNotExists(registryURL, registryList, REGISTRY_PROTOCOL)) {
                URL interfaceCompatibleRegistryURL = URLBuilder.from(registryURL)
                        .setProtocol(REGISTRY_PROTOCOL)
                        .removeParameter(REGISTRY_TYPE_KEY)
                        .build();
                result.add(interfaceCompatibleRegistryURL);
            }
        } else {
            registerMode = registryURL.getParameter(
                    REGISTER_MODE_KEY,
                    ConfigurationUtils.getCachedDynamicProperty(
                            scopeModel, DUBBO_REGISTER_MODE_DEFAULT_KEY, DEFAULT_REGISTER_MODE_ALL));
            if (!isValidRegisterMode(registerMode)) {
                registerMode = DEFAULT_REGISTER_MODE_INTERFACE;
            }
            if ((DEFAULT_REGISTER_MODE_INSTANCE.equalsIgnoreCase(registerMode)
                            || DEFAULT_REGISTER_MODE_ALL.equalsIgnoreCase(registerMode))
                    && registryNotExists(registryURL, registryList, SERVICE_REGISTRY_PROTOCOL)) {
                URL serviceDiscoveryRegistryURL = URLBuilder.from(registryURL)
                        .setProtocol(SERVICE_REGISTRY_PROTOCOL)
                        .removeParameter(REGISTRY_TYPE_KEY)
                        .build();
                result.add(serviceDiscoveryRegistryURL);
            }

            if (DEFAULT_REGISTER_MODE_INTERFACE.equalsIgnoreCase(registerMode)
                    || DEFAULT_REGISTER_MODE_ALL.equalsIgnoreCase(registerMode)) {
                result.add(registryURL);
            }
        }

        FrameworkStatusReportService reportService = ScopeModelUtil.getApplicationModel(scopeModel)
                .getBeanFactory()
                .getBean(FrameworkStatusReportService.class);
        reportService.reportRegistrationStatus(reportService.createRegistrationReport(registerMode));
    }
}
