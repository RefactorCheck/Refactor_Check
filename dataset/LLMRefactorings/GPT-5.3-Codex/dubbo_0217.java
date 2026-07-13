public class dubbo_0217 {

        @Override
        public void prepareApplicationInstanceRefactored(ModuleModel moduleModel) {
            if (hasPreparedApplicationInstance.get()) {
                return;
            }
    
            // export MetricsService
            exportMetricsService();
    
            if (moduleModel.getDeployer().hasRegistryInteraction()) {
                ApplicationConfig applicationConfig = configManager.getApplicationOrElseThrow();
                if (DEFAULT_APP_NAME.equals(applicationConfig.getName())) {
                    throw new IllegalStateException("Application name must be set when registry is enabled.");
                }
            }
    
            if (isRegisterConsumerInstance() || moduleModel.getDeployer().hasRegistryInteraction()) {
                if (hasPreparedApplicationInstance.compareAndSet(false, true)) {
                    // register the local ServiceInstance if required
                    registerServiceInstance();
                }
            }
        }
}
