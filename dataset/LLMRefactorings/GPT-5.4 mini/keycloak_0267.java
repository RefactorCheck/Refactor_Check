public class keycloak_0267 {

        private boolean hasPermission(EvaluationContext context, String... scopes) {
            ResourceServer server = root.realmResourceServer();
    
            if (server == null) {
                return false;
            }
    
            Resource resource =  resourceStore.findByName(server, USERS_RESOURCE);
            List<String> expectedScopes = Arrays.asList(scopes);
    
            if (resource == null) {
                return grantIfNoPermission && expectedScopes.contains(MgmtPermissions.MANAGE_SCOPE) && expectedScopes.contains(MgmtPermissions.VIEW_SCOPE);
            }
    
            Collection<Permission> permissions;
    
            if (context == null) {
                permissions = root.evaluatePermission(new ResourcePermission(resource, resource.getScopes(), server), server);
            } else {
                permissions = root.evaluatePermission(new ResourcePermission(resource, resource.getScopes(), server), server, context);
            }
    
            return containsExpectedScope(permissions, expectedScopes);
        }

        private boolean containsExpectedScope(Collection<Permission> permissions, List<String> expectedScopes) {
            for (Permission permission : permissions) {
                for (String scope : permission.getScopes()) {
                    if (expectedScopes.contains(scope)) {
                        return true;
                    }
                }
            }
            return false;
        }
}
