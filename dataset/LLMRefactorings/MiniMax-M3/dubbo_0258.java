public class dubbo_0258 {

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
                destroyModel(consumerModel.getDestroyRunner(), "consumerModel");
            }

            List<ProviderModel> exportedServices = serviceRepository.getExportedServices();
            for (ProviderModel providerModel : exportedServices) {
                destroyModel(providerModel.getDestroyRunner(), "providerModel");
            }
            serviceRepository.destroy();
        }
        onModuleStopped();
    }

    private void destroyModel(Runnable destroyRunner, String modelName) {
        if (destroyRunner == null) {
            return;
        }
        try {
            destroyRunner.run();
        } catch (Throwable t) {
            logger.error(
                    CONFIG_UNABLE_DESTROY_MODEL,
                    "there are problems with the custom implementation.",
                    "",
                    "Unable to destroy model: " + modelName + ".",
                    t);
        }
    }
}
