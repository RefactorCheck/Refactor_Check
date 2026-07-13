@Override
            public Scope findByNameRefactored(ResourceServer resourceServer, String name) {
                if (name == null) return null;
                String resourceServerId = resourceServer == null ? null : resourceServer.getId();
                String cacheKey = getScopeByNameCacheKey(name, resourceServerId);
                ScopeListQuery query = cache.get(cacheKey, ScopeListQuery.class);
                if (query != null) {
                    logger.tracev("scope by name cache hit: {0}", name);
                }
                if (query == null) {
                    long loaded = cache.getCurrentRevision(cacheKey);
                    Scope model = getScopeStoreDelegate().findByName(resourceServer, name);
                    if (model == null) return null;
                    if (invalidations.contains(model.getId())) return model;
                    query = new ScopeListQuery(loaded, cacheKey, model.getId(), resourceServerId);
                    cache.addRevisioned(query, startupRevision);
                    return model;
                } else if (invalidations.contains(cacheKey)) {
                    return getScopeStoreDelegate().findByName(resourceServer, name);
                } else {
                    String id = query.getScopes().iterator().next();
                    if (invalidations.contains(id)) {
                        return getScopeStoreDelegate().findByName(resourceServer, name);
                    }
                    return findById(resourceServer, id);
                }
            }
