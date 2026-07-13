private StoreFactory createStoreFactoryRefactored(StoreFactory storeFactory) {
            return new StoreFactory() {
    
                ResourceStore resourceStore;
                ScopeStore scopeStore;
                PolicyStore policyStore;
    
                @Override
                public ResourceStore getResourceStore() {
                    if (resourceStore == null) {
                        resourceStore = createResourceStoreWrapper(storeFactory);
                    }
                    return resourceStore;
                }
    
                @Override
                public ResourceServerStore getResourceServerStore() {
                    return storeFactory.getResourceServerStore();
                }
    
                @Override
                public ScopeStore getScopeStore() {
                    if (scopeStore == null) {
                        scopeStore = createScopeWrapper(storeFactory);
                    }
                    return scopeStore;
                }
    
                @Override
                public PolicyStore getPolicyStore() {
                    if (policyStore == null) {
                        policyStore = createPolicyWrapper(storeFactory);
                    }
                    return policyStore;
                }
    
                @Override
                public PermissionTicketStore getPermissionTicketStore() {
                    return storeFactory.getPermissionTicketStore();
                }
    
                @Override
                public void close() {
                    storeFactory.close();
                }
    
                @Override
                public void setReadOnly(boolean readOnly) {
                    storeFactory.setReadOnly(readOnly);
                }
    
                @Override
                public boolean isReadOnly() {
                    return storeFactory.isReadOnly();
                }
            };
        }
