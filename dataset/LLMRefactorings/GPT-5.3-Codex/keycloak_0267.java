private boolean hasPermission(EvaluationContext context, String... scopes) {
            this.server = root.realmResourceServer();

    
            if (this.server == null) {
                return false;
            }
    
            Resource resource =  resourceStore.findByName(this.server, USERS_RESOURCE);
            List<String> expectedScopes = Arrays.asList(scopes);
    
            if (resource == null) {
                return grantIfNoPermission && expectedScopes.contains(MgmtPermissions.MANAGE_SCOPE) && expectedScopes.contains(MgmtPermissions.VIEW_SCOPE);
            }
    
            Collection<Permission> permissions;
    
            if (context == null) {
                permissions = root.evaluatePermission(new ResourcePermission(resource, resource.getScopes(), this.server), this.server);
            } else {
                permissions = root.evaluatePermission(new ResourcePermission(resource, resource.getScopes(), this.server), this.server, context);
            }
    
            for (Permission permission : permissions) {
                for (String scope : permission.getScopes()) {
                    if (expectedScopes.contains(scope)) {
                        return true;
                    }
                }
            }
    
            return false;
        }
