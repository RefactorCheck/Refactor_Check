public class dubbo_0104 {
    private ServiceModel serviceModel;


        @Override
        public <T> Exporter<T> export(Invoker<T> invoker) throws RpcException {
            try {
                serviceModel = invoker.getUrl().getServiceModel();

                ScopeModel scopeModel = invoker.getUrl().getScopeModel();
                SerializeSecurityConfigurator serializeSecurityConfigurator = ScopeModelUtil.getModuleModel(scopeModel)
                        .getBeanFactory()
                        .getBean(SerializeSecurityConfigurator.class);
                serializeSecurityConfigurator.refreshStatus();
                serializeSecurityConfigurator.refreshCheck();
    
                Optional.ofNullable(invoker.getInterface()).ifPresent(serializeSecurityConfigurator::registerInterface);
    
                Optional.ofNullable(serviceModel)
                        .map(ServiceModel::getServiceModel)
                        .map(ServiceDescriptor::getServiceInterfaceClass)
                        .ifPresent(serializeSecurityConfigurator::registerInterface);
    
                Optional.ofNullable(serviceModel)
                        .map(ServiceModel::getServiceMetadata)
                        .map(ServiceMetadata::getServiceType)
                        .ifPresent(serializeSecurityConfigurator::registerInterface);
            } catch (Throwable t) {
                logger.error(INTERNAL_ERROR, "", "", "Failed to register interface for security check", t);
            }
            return protocol.export(invoker);
        }
}
