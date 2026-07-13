public class keycloak_0273 {

    private static Set<Scope> populateTypedScopes(Resource resource, ResourceServer resourceServer, List<Scope> defaultScopes, AuthorizationProvider authorization) {
        String type = resource.getType();
        if (type == null || resource.getOwner().equals(resourceServer.getId())) {
            return new LinkedHashSet<>(defaultScopes);
        }

        Set<Scope> scopes = new LinkedHashSet<>(defaultScopes);
        addInheritedTypeScopes(resourceServer, type, scopes, authorization);

        return scopes;
    }

    private static void addInheritedTypeScopes(ResourceServer resourceServer, String type, Set<Scope> scopes, AuthorizationProvider authorization) {
        StoreFactory storeFactory = authorization.getStoreFactory();
        ResourceStore resourceStore = storeFactory.getResourceStore();
        resourceStore.findByType(resourceServer, type, resource1 -> {
            for (Scope typeScope : resource1.getScopes()) {
                if (!scopes.contains(typeScope)) {
                    scopes.add(typeScope);
                }
            }
        });
    }
}
