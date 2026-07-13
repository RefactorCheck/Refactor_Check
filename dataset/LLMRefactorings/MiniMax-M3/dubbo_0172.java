public class dubbo_0172 {

        @Override
        protected void postProcessAfterScopeModelChanged(ScopeModel oldScopeModel, ScopeModel newScopeModel) {
            super.postProcessAfterScopeModelChanged(oldScopeModel, newScopeModel);
            ApplicationModel applicationModel = ScopeModelUtil.getApplicationModel(getScopeModel());
            updateConfigScopeModel(this.configCenter, applicationModel);
            updateConfigScopeModel(this.metadataReportConfig, applicationModel);
            updateConfigScopeModel(this.monitor, applicationModel);
            if (CollectionUtils.isNotEmpty(this.registries)) {
                this.registries.forEach(registryConfig -> updateConfigScopeModel(registryConfig, applicationModel));
            }
        }

        private void updateConfigScopeModel(AbstractConfig config, ApplicationModel applicationModel) {
            if (config != null && config.getScopeModel() != applicationModel) {
                config.setScopeModel(applicationModel);
            }
        }
}
