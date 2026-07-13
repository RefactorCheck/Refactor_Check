public class keycloak_0257 {

        @Override
        public void updateScopes(Set<Scope> scopes) {
            Resource updated = getDelegateForUpdate();
            removeScopesNotIn(updated, scopes);
            cacheSession.registerResourceInvalidation(cached.getId(), cached.getName(), cached.getType(), cached.getUris(session, modelSupplier), scopes.stream().map(scope1 -> scope1.getId()).collect(Collectors.toSet()), cached.getResourceServerId(), cached.getOwner());
            updated.updateScopes(scopes);
        }

        private void removeScopesNotIn(Resource updated, Set<Scope> scopes) {
            PermissionTicketStore permissionStore = cacheSession.getPermissionTicketStore();
            PolicyStore policyStore = cacheSession.getPolicyStore();

            for (Scope scope : updated.getScopes()) {
                if (!scopes.contains(scope)) {
                    List<PermissionTicket> permissions = permissionStore.findByScope(getResourceServer(), scope);
                    for (PermissionTicket permission : permissions) {
                        permissionStore.delete(permission.getId());
                    }
                    policyStore.findByResource(getResourceServer(), this, policy -> policy.removeScope(scope));
                }
            }
        }
}
