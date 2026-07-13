public class keycloak_0086 {

        protected <T> T getStorageProviderInstance(StorageProviderModelType model, Class<T> capabilityInterface, boolean includeDisabled) {
            if (model == null || (!model.isEnabled() && !includeDisabled) || capabilityInterface == null) {
                return null;
            }
    
            @SuppressWarnings("unchecked")
            ProviderType instance = (ProviderType) session.getAttribute(model.getId());
            if (instance != null && capabilityInterface.isAssignableFrom(instance.getClass())) return capabilityInterface.cast(instance);
    
            ComponentFactory<? extends ProviderType, ProviderType> factory = getStorageProviderFactory(model.getProviderId());
            if (factory == null) {
                LOG.warnv("Configured StorageProvider {0} of provider id {1} does not exist", model.getName(), model.getProviderId());
                return null;
            }
            if (!Types.supports(capabilityInterface, factory, factoryTypeClass)) {
                return null;
            }
    
            instance = factory.create(session, model);
            if (instance == null) {
                throw new IllegalStateException("StorageProviderFactory (of type " + factory.getClass().getName() + ") produced a null instance");
            }
            session.enlistForClose(instance);
            session.setAttribute(model.getId(), instance);
            return capabilityInterface.cast(instance);
        }
}
