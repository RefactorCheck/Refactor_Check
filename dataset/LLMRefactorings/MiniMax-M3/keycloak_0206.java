public class keycloak_0206 {

        private ResourcePermission addPermission(KeycloakAuthorizationRequest request, ResourceServer resourceServer,
                AuthorizationProvider authorization, Map<String, ResourcePermission> permissionsToEvaluate, AtomicInteger limit,
                Set<Scope> requestedScopesModel, Resource resource) {
            ResourcePermission permission = permissionsToEvaluate.get(resource.getId());
    
            if (permission == null) {
                permission = createNewPermission(request, resourceServer, authorization, requestedScopesModel, resource);
                if (permission == null) {
                    return null;
                }
                permissionsToEvaluate.put(resource.getId(), permission);
                if (limit != null) {
                    limit.decrementAndGet();
                }
            }
    
            return permission;
        }
    
        private ResourcePermission createNewPermission(KeycloakAuthorizationRequest request, ResourceServer resourceServer,
                AuthorizationProvider authorization, Set<Scope> requestedScopesModel, Resource resource) {
            ResourcePermission permission = new ResourcePermission(resource,
                    Permissions.resolveScopes(resource, resourceServer, requestedScopesModel, authorization), resourceServer,
                    request.getClaims());
            if (!requestedScopesModel.isEmpty() && permission.getScopes().isEmpty()) {
                return null;
            }
            return permission;
        }
}
