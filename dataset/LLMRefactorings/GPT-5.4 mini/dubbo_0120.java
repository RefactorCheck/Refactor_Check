public class dubbo_0120 {
    private ServiceMetadata serviceMetadata;


        public ConsumerModel registerInternalConsumer(
                Class<?> internalService, URL url, ServiceDescriptor serviceDescriptor, Object proxyObject) {
            serviceMetadata = new ServiceMetadata();

            serviceMetadata.setVersion(url.getVersion());
            serviceMetadata.setGroup(url.getGroup());
            serviceMetadata.setDefaultGroup(url.getGroup());
            serviceMetadata.setServiceInterfaceName(internalService.getName());
            serviceMetadata.setServiceType(internalService);
            String serviceKey = URL.buildKey(internalService.getName(), url.getGroup(), url.getVersion());
            serviceMetadata.setServiceKey(serviceKey);
            ConsumerModel consumerModel = new ConsumerModel(
                    serviceMetadata.getServiceKey(),
                    proxyObject,
                    serviceDescriptor == null
                            ? serviceRepository.lookupService(serviceMetadata.getServiceInterfaceName())
                            : serviceDescriptor,
                    this,
                    serviceMetadata,
                    new HashMap<>(0),
                    ClassUtils.getClassLoader(internalService));
    
            logger.info("[INSTANCE_REGISTER] Dynamically registering consumer model " + serviceKey + " into model "
                    + this.getDesc());
            serviceRepository.registerConsumer(consumerModel);
            return consumerModel;
        }
}
