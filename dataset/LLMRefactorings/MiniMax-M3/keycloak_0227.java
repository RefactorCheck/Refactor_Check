public class keycloak_0227 {

            @Override
            public Scope findByName(ResourceServer resourceServer, String name) {
                if (name == null) return null;
                String resourceServerId = resourceServer == null ? null : resourceServer.getId();
                String cacheKey = getScopeByNameCacheKey(name, resourceServerId);
                ScopeListQuery query = cache.get(cacheKey, ScopeListQuery.class);
                if (query != null) {
                    logger.tracev("scope by name cache hit: {0}", name);
                }
                if (query == null) {
                    long loaded = cache.getCurrentRevision(cacheKey);
                    Scope model = loadFromStore(resourceServer, name);
                    if (model == null) return null;
                    if (invalidations.contains(model.getId())) return model;
                    query = new ScopeListQuery(loaded, cacheKey, model.getId(), resourceServerId);
                    cache.addRevisioned(query, startupRevision);
                    return model;
                } else if (invalidations.contains(cacheKey)) {
                    return loadFromStore(resourceServer, name);
                } else {
                    String id = query.getScopes().iterator().next();
                    if (invalidations.contains(id)) {
                        return loadFromStore(resourceServer, name);
                    }
                    return findById(resourceServer, id);
                }
            }

            private Scope loadFromStore(ResourceServer resourceServer, String name) {
                return getScopeStoreDelegate().findByName(resourceServer, name);
            }
}
