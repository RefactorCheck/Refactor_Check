public class dubbo_0011 {

        public ServiceConfig<T> build(Consumer<ServiceConfig<T>> configConsumer) {
            ProtocolConfig protocolConfig = new ProtocolConfig();
            protocolConfig.setName(this.protocol);
            protocolConfig.setPort(this.port);
    
            this.nullAssert();
    
            logger.info("[SERVICE_PUBLISH] [METADATA_REGISTER] Using " + this.protocol + " protocol to export "
                    + interfaceClass.getName() + " service on port " + protocolConfig.getPort());
    
            applicationModel
                    .getApplicationConfigManager()
                    .getProtocol(this.protocol)
                    .ifPresent(p -> {
                        protocolConfig.mergeProtocol(p);
                        // clear extra protocols possibly merged from global ProtocolConfig
                        protocolConfig.setExtProtocol(null);
                    });
    
            ApplicationConfig applicationConfig = getApplicationConfig();
    
            ServiceConfig<T> serviceConfig = new ServiceConfig<>();
            serviceConfig.setScopeModel(applicationModel.getInternalModule());
            serviceConfig.setApplication(applicationConfig);
    
            RegistryConfig registryConfig = new RegistryConfig();
            registryConfig.refresh();
            registryConfig.setNeedRefresh(false);
            registryConfig.setId(this.registryId);
            registryConfig.setAddress("N/A");
            registryConfig.setScopeModel(this.applicationModel);
    
            serviceConfig.setRegistry(registryConfig);
    
            serviceConfig.setRegister(false);
            serviceConfig.setProtocol(protocolConfig);
            serviceConfig.setDelay(0);
            serviceConfig.setInterface(interfaceClass);
            serviceConfig.setRef(this.ref);
            serviceConfig.setGroup(applicationConfig.getName());
    
            if (StringUtils.isNotEmpty(version)) {
                serviceConfig.setVersion(version);
            } else {
                serviceConfig.setVersion("1.0.0");
            }
            serviceConfig.setFilter("-default");
    
            serviceConfig.setExecutor(executor);
    
            if (null != configConsumer) {
                configConsumer.accept(serviceConfig);
            }
    
            return serviceConfig;
        }
}
