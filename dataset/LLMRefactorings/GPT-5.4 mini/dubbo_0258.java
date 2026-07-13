public static class dubbo_0258 {

        @Override
        public synchronized void postDestroy() throws IllegalStateException {
            if (isStopped()) {
                return;
            }
            unexportServices();
            unreferServices();
    
            ModuleServiceRepository serviceRepository = moduleModel.getServiceRepository();
            if (serviceRepository != null) {
                List<ConsumerModel> consumerModels = serviceRepository.getReferredServices();
    
                for (ConsumerModel consumerModel : consumerModels) {
                    try {
                        if (consumerModel.getDestroyRunner() != null) {
                            consumerModel.getDestroyRunner().run();
                        }
                    } catch (Throwable t) {
                        logger.error(
                                CONFIG_UNABLE_DESTROY_MODEL,
                                "there are problems with the custom implementation.",
                                "",
                                "Unable to destroy model: consumerModel.",
                                t);
                    }
                }
    
                List<ProviderModel> exportedServices = serviceRepository.getExportedServices();
                for (ProviderModel providerModel : exportedServices) {
                    try {
                        if (providerModel.getDestroyRunner() != null) {
                            providerModel.getDestroyRunner().run();
                        }
                    } catch (Throwable t) {
                        logger.error(
                                CONFIG_UNABLE_DESTROY_MODEL,
                                "there are problems with the custom implementation.",
                                "",
                                "Unable to destroy model: providerModel.",
                                t);
                    }
                }
                serviceRepository.destroy();
            }
            onModuleStopped();
        }
}
