public class dubbo_0172 {

        @Override
        protected void postProcessAfterScopeModelChanged(ScopeModel oldScopeModel, ScopeModel newScopeModel) {
            super.postProcessAfterScopeModelChanged(oldScopeModel, newScopeModel);
            // change referenced config's scope model
            ApplicationModel applicationModel = ScopeModelUtil.getApplicationModel(getScopeModel());
            if (this.configCenter != null && this.configCenter.getScopeModel() != applicationModel) {
                this.configCenter.setScopeModel(applicationModel);
            }
            if (this.metadataReportConfig != null && this.metadataReportConfig.getScopeModel() != applicationModel) {
                this.metadataReportConfig.setScopeModel(applicationModel);
            }
            if (this.monitor != null && this.monitor.getScopeModel() != applicationModel) {
                this.monitor.setScopeModel(applicationModel);
            }
            if (CollectionUtils.isNotEmpty(this.registries)) {
                this.registries.forEach(registryConfig -> {
                    if (registryConfig.getScopeModel() != applicationModel) {
                        registryConfig.setScopeModel(applicationModel);
                    }
                });
            }
        }
}
