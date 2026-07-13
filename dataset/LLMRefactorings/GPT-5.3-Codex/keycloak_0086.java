protected <T> T getStorageProviderInstance(StorageProviderModelType model, Class<T> capabilityInterface, boolean includeDisabled) {
            if (model == null || (!model.isEnabled() && !includeDisabled) || capabilityInterface == null) {
                return null;
            }
    
            @SuppressWarnings("unchecked")

            if (((ProviderType) session.getAttribute(model.getId())) != null && capabilityInterface.isAssignableFrom(((ProviderType) session.getAttribute(model.getId())).getClass())) return capabilityInterface.cast(((ProviderType) session.getAttribute(model.getId())));
    
            ComponentFactory<? extends ProviderType, ProviderType> factory = getStorageProviderFactory(model.getProviderId());
            if (factory == null) {
                LOG.warnv("Configured StorageProvider {0} of provider id {1} does not exist", model.getName(), model.getProviderId());
                return null;
            }
            if (!Types.supports(capabilityInterface, factory, factoryTypeClass)) {
                return null;
            }
    
            ((ProviderType) session.getAttribute(model.getId())) = factory.create(session, model);
            if (((ProviderType) session.getAttribute(model.getId())) == null) {
                throw new IllegalStateException("StorageProviderFactory (of type " + factory.getClass().getName() + ") produced a null ((ProviderType) session.getAttribute(model.getId()))");
            }
            session.enlistForClose(((ProviderType) session.getAttribute(model.getId())));
            session.setAttribute(model.getId(), ((ProviderType) session.getAttribute(model.getId())));
            return capabilityInterface.cast(((ProviderType) session.getAttribute(model.getId())));
        }
