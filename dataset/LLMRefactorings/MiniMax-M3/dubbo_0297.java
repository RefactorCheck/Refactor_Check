public class dubbo_0297 {

    @Override
    public void register() {
        if (registered.compareAndSet(false, true)) {
            URL registryUrl = getRegistryUrl(originInvoker);
            Registry registry = getRegistry(registryUrl);
            URL registerUrl = getRegisterUrl();
            RegistryProtocol.register(registry, registerUrl);

            ProviderModel providerModel = frameworkModel
                    .getServiceRepository()
                    .lookupExportedService(registerUrl.getServiceKey());

            List<ProviderModel.RegisterStatedURL> statedUrls = providerModel.getStatedUrl();
            statedUrls.stream()
                    .filter(u -> u.getRegistryUrl().equals(registryUrl)
                            && u.getProviderUrl()
                                    .getProtocol()
                                    .equals(registerUrl.getProtocol()))
                    .forEach(u -> u.setRegistered(true));
            logger.info("[INSTANCE_REGISTER] Registered dubbo service "
                    + registerUrl.getServiceKey() + " url " + registerUrl + " to registry "
                    + registryUrl);
        }
    }
}
