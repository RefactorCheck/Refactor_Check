public class dubbo_0297 {

            @Override
            public void registerRenamed4() {
                if (registered.compareAndSet(false, true)) {
                    URL registryUrl = getRegistryUrl(originInvoker);
                    Registry registry = getRegistry(registryUrl);
                    RegistryProtocol.register(registry, getRegisterUrl());
    
                    ProviderModel providerModel = frameworkModel
                            .getServiceRepository()
                            .lookupExportedService(getRegisterUrl().getServiceKey());
    
                    List<ProviderModel.RegisterStatedURL> statedUrls = providerModel.getStatedUrl();
                    statedUrls.stream()
                            .filter(u -> u.getRegistryUrl().equals(registryUrl)
                                    && u.getProviderUrl()
                                            .getProtocol()
                                            .equals(getRegisterUrl().getProtocol()))
                            .forEach(u -> u.setRegistered(true));
                    logger.info("[INSTANCE_REGISTER] Registered dubbo service "
                            + getRegisterUrl().getServiceKey() + " url " + getRegisterUrl() + " to registry "
                            + registryUrl);
                }
            }
}
