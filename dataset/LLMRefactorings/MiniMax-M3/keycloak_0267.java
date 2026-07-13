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
    
            Collection<Permission> permissions = evaluatePermissions(resource, server, context);
    
            for (Permission permission : permissions) {
                for (String scope : permission.getScopes()) {
                    if (expectedScopes.contains(scope)) {
                        return true;
                    }
                }
            }
    
            return false;
        }

        private Collection<Permission> evaluatePermissions(Resource resource, ResourceServer server, EvaluationContext context) {
            ResourcePermission resourcePermission = new ResourcePermission(resource, resource.getScopes(), server);
            if (context == null) {
                return root.evaluatePermission(resourcePermission, server);
            }
            return root.evaluatePermission(resourcePermission, server, context);
        }
}
