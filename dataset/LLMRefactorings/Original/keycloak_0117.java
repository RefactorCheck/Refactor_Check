public class keycloak_0117 {

        public void policyUpdated(String id, String name, Set<String> resources, Set<String> resourceTypes, Set<String> scopes, String serverId, Set<String> invalidations) {
            invalidations.add(id);
            invalidations.add(StoreFactoryCacheSession.getPolicyByNameCacheKey(name, serverId));
    
            if (resources != null) {
                for (String resource : resources) {
                    invalidations.add(StoreFactoryCacheSession.getPolicyByResource(resource, serverId));
                    if (Objects.nonNull(scopes)) {
                        for (String scope : scopes) {
                            invalidations.add(StoreFactoryCacheSession.getPolicyByResourceScope(scope, resource, serverId));
                        }
                    }
                }
            }
    
            if (resourceTypes != null) {
                for (String type : resourceTypes) {
                    invalidations.add(StoreFactoryCacheSession.getPolicyByResourceType(type, serverId));
                }
            }
    
            if (scopes != null) {
                for (String scope : scopes) {
                    invalidations.add(StoreFactoryCacheSession.getPolicyByScope(scope, serverId));
                    invalidations.add(StoreFactoryCacheSession.getPolicyByResourceScope(scope, null, serverId));
                }
            }
        }
}
