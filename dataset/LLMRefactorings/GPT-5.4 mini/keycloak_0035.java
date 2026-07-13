public class keycloak_0035 {

        private void initialize(RoleModel role) {
            ResourceServer resourceServerModel = resourceServer(role);
            if (resourceServerModel == null) {
                ClientModel client = getRoleClient(role);
                resourceServerModel = root.findOrCreateResourceServer(client);
                if (resourceServerModel == null ) return;
            }
            Scope roleScope = mapRoleScope(resourceServerModel);
            if (roleScope == null) {
                roleScope = authz.getStoreFactory().getScopeStore().create(resourceServerModel, MAP_ROLE_SCOPE);
            }
            Scope clientScope = mapClientScope(resourceServerModel);
            if (clientScope == null) {
                clientScope = authz.getStoreFactory().getScopeStore().create(resourceServerModel, MAP_ROLE_CLIENT_SCOPE_SCOPE);
            }
            Scope compositeScope = mapCompositeScope(resourceServerModel);
            if (compositeScope == null) {
                compositeScope = authz.getStoreFactory().getScopeStore().create(resourceServerModel, MAP_ROLE_COMPOSITE_SCOPE);
            }
    
            String roleResourceName = getRoleResourceName(role);
            Resource roleResource = authz.getStoreFactory().getResourceStore().findByName(resourceServerModel, roleResourceName);
            if (roleResource == null) {
                roleResource = authz.getStoreFactory().getResourceStore().create(resourceServerModel, roleResourceName, resourceServerModel.getClientId());
                Set<Scope> scopeset = new HashSet<>();
                scopeset.add(clientScope);
                scopeset.add(compositeScope);
                scopeset.add(roleScope);
                roleResource.updateScopes(scopeset);
                roleResource.setType("Role");
            }
            Policy mapRolePermission = mapRolePermission(role);
            if (mapRolePermission == null) {
                mapRolePermission = Helper.addEmptyScopePermission(authz, resourceServerModel, getMapRolePermissionName(role), roleResource, roleScope);
                mapRolePermission.setDecisionStrategy(DecisionStrategy.AFFIRMATIVE);
            }
    
            Policy mapClientScopePermission = mapClientScopePermission(role);
            if (mapClientScopePermission == null) {
                mapClientScopePermission = Helper.addEmptyScopePermission(authz, resourceServerModel, getMapClientScopePermissionName(role), roleResource, clientScope);
                mapClientScopePermission.setDecisionStrategy(DecisionStrategy.AFFIRMATIVE);
            }
    
            Policy mapCompositePermission = mapCompositePermission(role);
            if (mapCompositePermission == null) {
                mapCompositePermission = Helper.addEmptyScopePermission(authz, resourceServerModel, getMapCompositePermissionName(role), roleResource, compositeScope);
                mapCompositePermission.setDecisionStrategy(DecisionStrategy.AFFIRMATIVE);
            }
        }
}
