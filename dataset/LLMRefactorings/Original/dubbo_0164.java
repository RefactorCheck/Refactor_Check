public class dubbo_0164 {

        @SuppressWarnings({"unchecked", "rawtypes"})
        private void doExportUrls(RegisterTypeEnum registerType) {
            ModuleServiceRepository repository = getScopeModel().getServiceRepository();
            ServiceDescriptor serviceDescriptor;
            final boolean serverService = ref instanceof ServerService;
            if (serverService) {
                serviceDescriptor = ((ServerService) ref).getServiceDescriptor();
                if (!this.provider.getUseJavaPackageAsPath()) {
                    // for stub service, path always interface name or IDL package name
                    this.path = serviceDescriptor.getInterfaceName();
                }
                repository.registerService(serviceDescriptor);
            } else {
                serviceDescriptor = repository.registerService(getInterfaceClass());
            }
            providerModel = new ProviderModel(
                    serviceMetadata.getServiceKey(),
                    ref,
                    serviceDescriptor,
                    getScopeModel(),
                    serviceMetadata,
                    interfaceClassLoader);
    
            // Compatible with dependencies on ServiceModel#getServiceConfig(), and will be removed in a future version
            providerModel.setConfig(this);
    
            providerModel.setDestroyRunner(getDestroyRunner());
            repository.registerProvider(providerModel);
    
            List<URL> registryURLs = !Boolean.FALSE.equals(isRegister())
                    ? ConfigValidationUtils.loadRegistries(this, true)
                    : Collections.emptyList();
    
            for (ProtocolConfig protocolConfig : protocols) {
                String pathKey = URL.buildKey(
                        getContextPath(protocolConfig).map(p -> p + "/" + path).orElse(path), group, version);
                // stub service will use generated service name
                if (!serverService) {
                    // In case user specified path, register service one more time to map it to path.
                    repository.registerService(pathKey, interfaceClass);
                }
                doExportUrlsFor1Protocol(protocolConfig, registryURLs, registerType);
            }
    
            providerModel.setServiceUrls(urls);
        }
}
