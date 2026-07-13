public class keycloak_0035 {

    private void initialize(RoleModel role) {
        ResourceServer server = resourceServer(role);
        if (server == null) {
            ClientModel client = getRoleClient(role);
            server = root.findOrCreateResourceServer(client);
            if (server == null) return;
        }
        Scope mapRoleScope = getOrCreateScope(server, mapRoleScope(server), MAP_ROLE_SCOPE);
        Scope mapClientScope = getOrCreateScope(server, mapClientScope(server), MAP_ROLE_CLIENT_SCOPE_SCOPE);
        Scope mapCompositeScope = getOrCreateScope(server, mapCompositeScope(server), MAP_ROLE_COMPOSITE_SCOPE);

        String roleResourceName = getRoleResourceName(role);
        Resource resource = authz.getStoreFactory().getResourceStore().findByName(server, roleResourceName);
        if (resource == null) {
            resource = authz.getStoreFactory().getResourceStore().create(server, roleResourceName, server.getClientId());
            Set<Scope> scopeset = new HashSet<>();
            scopeset.add(mapClientScope);
            scopeset.add(mapCompositeScope);
            scopeset.add(mapRoleScope);
            resource.updateScopes(scopeset);
            resource.setType("Role");
        }
        Policy mapRolePermission = mapRolePermission(role);
        if (mapRolePermission == null) {
            mapRolePermission = Helper.addEmptyScopePermission(authz, server, getMapRolePermissionName(role), resource, mapRoleScope);
            mapRolePermission.setDecisionStrategy(DecisionStrategy.AFFIRMATIVE);
        }

        Policy mapClientScopePermission = mapClientScopePermission(role);
        if (mapClientScopePermission == null) {
            mapClientScopePermission = Helper.addEmptyScopePermission(authz, server, getMapClientScopePermissionName(role), resource, mapClientScope);
            mapClientScopePermission.setDecisionStrategy(DecisionStrategy.AFFIRMATIVE);
        }

        Policy mapCompositePermission = mapCompositePermission(role);
        if (mapCompositePermission == null) {
            mapCompositePermission = Helper.addEmptyScopePermission(authz, server, getMapCompositePermissionName(role), resource, mapCompositeScope);
            mapCompositePermission.setDecisionStrategy(DecisionStrategy.AFFIRMATIVE);
        }
    }

    private Scope getOrCreateScope(ResourceServer server, Scope existingScope, String scopeName) {
        if (existingScope == null) {
            return authz.getStoreFactory().getScopeStore().create(server, scopeName);
        }
        return existingScope;
    }
}
