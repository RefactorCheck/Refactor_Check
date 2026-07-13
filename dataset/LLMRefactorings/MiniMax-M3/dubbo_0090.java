public class dubbo_0090 {

        @Override
        public void customize(ServiceInstance serviceInstance, ApplicationModel applicationModel) {
            MetadataInfo metadataInfo = serviceInstance.getServiceMetadata();
            if (metadataInfo == null || CollectionUtils.isEmptyMap(metadataInfo.getServices())) {
                return;
            }
    
            Map<String, String> extraParameters = loadExtraParameters(applicationModel);
            serviceInstance.getMetadata().putAll(extraParameters);
            if (CollectionUtils.isNotEmptyMap(metadataInfo.getInstanceParams())) {
                serviceInstance.getMetadata().putAll(metadataInfo.getInstanceParams());
            }
        }

        private Map<String, String> loadExtraParameters(ApplicationModel applicationModel) {
            Map<String, String> extraParameters = Collections.emptyMap();
            Set<InfraAdapter> adapters =
                    applicationModel.getExtensionLoader(InfraAdapter.class).getSupportedExtensionInstances();
            if (CollectionUtils.isNotEmpty(adapters)) {
                Map<String, String> inputParameters = new HashMap<>();
                inputParameters.put(APPLICATION_KEY, applicationModel.getApplicationName());
                for (InfraAdapter adapter : adapters) {
                    extraParameters = adapter.getExtraAttributes(inputParameters);
                }
            }
            return extraParameters;
        }
}
