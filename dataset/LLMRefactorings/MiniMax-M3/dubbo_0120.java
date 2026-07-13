public class dubbo_0120 {

        public ConsumerModel registerInternalConsumer(
                Class<?> internalService, URL url, ServiceDescriptor serviceDescriptor, Object proxyObject) {
            ServiceMetadata serviceMetadata = buildServiceMetadata(internalService, url);
            ConsumerModel consumerModel = createConsumerModel(
                    serviceMetadata, serviceDescriptor, proxyObject, internalService);

            logger.info("[INSTANCE_REGISTER] Dynamically registering consumer model "
                    + serviceMetadata.getServiceKey() + " into model " + this.getDesc());
            serviceRepository.registerConsumer(consumerModel);
            return consumerModel;
        }

        private ServiceMetadata buildServiceMetadata(Class<?> internalService, URL url) {
            ServiceMetadata serviceMetadata = new ServiceMetadata();
            serviceMetadata.setVersion(url.getVersion());
            serviceMetadata.setGroup(url.getGroup());
            serviceMetadata.setDefaultGroup(url.getGroup());
            serviceMetadata.setServiceInterfaceName(internalService.getName());
            serviceMetadata.setServiceType(internalService);
            String serviceKey = URL.buildKey(internalService.getName(), url.getGroup(), url.getVersion());
            serviceMetadata.setServiceKey(serviceKey);
            return serviceMetadata;
        }

        private ConsumerModel createConsumerModel(
                ServiceMetadata serviceMetadata,
                ServiceDescriptor serviceDescriptor,
                Object proxyObject,
                Class<?> internalService) {
            return new ConsumerModel(
                    serviceMetadata.getServiceKey(),
                    proxyObject,
                    serviceDescriptor == null
                            ? serviceRepository.lookupService(serviceMetadata.getServiceInterfaceName())
                            : serviceDescriptor,
                    this,
                    serviceMetadata,
                    new HashMap<>(0),
                    ClassUtils.getClassLoader(internalService));
        }
}
