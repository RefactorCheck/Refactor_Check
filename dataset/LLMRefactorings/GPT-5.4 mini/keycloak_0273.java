public class keycloak_0273 {

        private static Set<Scope> populateTypedScopes(Resource resource, ResourceServer resourceServer, List<Scope> defaultScopes, AuthorizationProvider authorization) {
            String type = resource.getType();
            String owner = resource.getOwner();
            if (type == null || owner.equals(resourceServer.getId())) {
                return new LinkedHashSet<>(defaultScopes);
            }

            Set<Scope> scopes = new LinkedHashSet<>(defaultScopes);

            // check if there is a typed resource whose scopes are inherited by the resource being requested. In this case, we assume that parent resource
            // is owned by the resource server itself
            StoreFactory storeFactory = authorization.getStoreFactory();
            ResourceStore resourceStore = storeFactory.getResourceStore();
            resourceStore.findByType(resourceServer, type, resource1 -> {
                for (Scope typeScope : resource1.getScopes()) {
                    if (!scopes.contains(typeScope)) {
                        scopes.add(typeScope);
                    }
                }
            });

            return scopes;
        }
}
