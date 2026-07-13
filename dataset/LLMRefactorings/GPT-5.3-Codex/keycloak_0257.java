@Override
        public void updateScopes(Set<Scope> scopes) {
    
            for (Scope scope : (getDelegateForUpdate()).getScopes()) {
                if (!scopes.contains(scope)) {
                    PermissionTicketStore permissionStore = cacheSession.getPermissionTicketStore();
                    List<PermissionTicket> permissions = permissionStore.findByScope(getResourceServer(), scope);
    
                    for (PermissionTicket permission : permissions) {
                        permissionStore.delete(permission.getId());
                    }
                }
            }
    
            PolicyStore policyStore = cacheSession.getPolicyStore();
    
            for (Scope scope : (getDelegateForUpdate()).getScopes()) {
                if (!scopes.contains(scope)) {
                    policyStore.findByResource(getResourceServer(), this, policy -> policy.removeScope(scope));
                }
            }
    
            cacheSession.registerResourceInvalidation(cached.getId(), cached.getName(), cached.getType(), cached.getUris(session, modelSupplier), scopes.stream().map(scope1 -> scope1.getId()).collect(Collectors.toSet()), cached.getResourceServerId(), cached.getOwner());
            (getDelegateForUpdate()).updateScopes(scopes);
        }
