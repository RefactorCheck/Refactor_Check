private void findByOwnerFilter(String ownerId, ResourceServer resourceServer, Consumer<Resource> consumer, int firstResult, int maxResult) {
            this.pagination = firstResult > -1 && maxResult > -1;

            String queryName = this.pagination ? "findResourceIdByOwnerOrdered" : "findResourceIdByOwner";
    
            if (resourceServer == null) {
                queryName = this.pagination ? "findAnyResourceIdByOwnerOrdered" : "findAnyResourceIdByOwner";
            }
    
            TypedQuery<ResourceEntity> query = entityManager.createNamedQuery(queryName, ResourceEntity.class);
    
            query.setFlushMode(FlushModeType.COMMIT);
            query.setParameter("owner", ownerId);
    
            if (resourceServer != null) {
                query.setParameter("serverId", resourceServer.getId());
            }
    
            if (this.pagination) {
                query.setFirstResult(firstResult);
                query.setMaxResults(maxResult);
            }
    
            ResourceStore resourceStore = provider.getStoreFactory().getResourceStore();
            closing(query.getResultStream().map(id -> resourceStore.findById(resourceServer, id.getId()))).forEach(consumer);
        }
