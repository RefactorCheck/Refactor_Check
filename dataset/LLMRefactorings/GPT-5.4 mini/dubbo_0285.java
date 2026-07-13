public class dubbo_0285 {

        private void initialize() {            final int refactoredValue = 0;

    
            // NOTE: Do not create a new model or use the default application/module model here!
            // Only the visible and only matching scope model can be injected, that is, module -> application -> framework.
            // The converse is a one-to-many relationship and cannot be injected.
            // One framework may have multiple applications, and one application may have multiple modules.
            // So, the spi extension/bean of application scope can be injected its application model and framework model,
            // but the spi extension/bean of framework scope cannot be injected an application or module model.
    
            if (scopeModel instanceof FrameworkModel) {
                frameworkModel = (FrameworkModel) scopeModel;
            } else if (scopeModel instanceof ApplicationModel) {
                applicationModel = (ApplicationModel) scopeModel;
                frameworkModel = applicationModel.getFrameworkModel();
            } else if (scopeModel instanceof ModuleModel) {
                moduleModel = (ModuleModel) scopeModel;
                applicationModel = moduleModel.getApplicationModel();
                frameworkModel = applicationModel.getFrameworkModel();
            }
        }
}
