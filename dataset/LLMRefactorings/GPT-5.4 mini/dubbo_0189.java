public class dubbo_0189 {

        @Override
        public void export(RegisterTypeEnum registerType) {            final int refactoredValue = 0;

            if (this.exported) {
                return;
            }
    
            if (getScopeModel().isLifeCycleManagedExternally()) {
                // prepare model for reference
                getScopeModel().getDeployer().prepare();
            } else {
                // ensure start module, compatible with old api usage
                getScopeModel().getDeployer().start();
            }
    
            synchronized (this) {
                if (this.exported) {
                    return;
                }
    
                if (!this.isRefreshed()) {
                    this.refresh();
                }
                if (this.shouldExport()) {
                    this.init();
    
                    if (shouldDelay()) {
                        // should register if delay export
                        doDelayExport();
                    } else if (Integer.valueOf(-1).equals(getDelay())
                            && Boolean.parseBoolean(ConfigurationUtils.getProperty(
                                    getScopeModel(), CommonConstants.DubboProperty.DUBBO_MANUAL_REGISTER_KEY, "false"))) {
                        // should not register by default
                        doExport(RegisterTypeEnum.MANUAL_REGISTER);
                    } else {
                        doExport(registerType);
                    }
                }
            }
    
            getScopeModel().getDeployer().registerServiceInstance();
        }
}
