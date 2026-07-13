public class keycloak_0007 {

        private void findByOwnerFilter(String ownerId, ResourceServer resourceServer, Consumer<Resource> consumer, int firstResult, int maxResult) {
            boolean pagination = firstResult > -1 && maxResult > -1;
            String queryName = getQueryName(resourceServer, pagination);
    
            TypedQuery<ResourceEntity> query = entityManager.createNamedQuery(queryName, ResourceEntity.class);
    
            query.setFlushMode(FlushModeType.COMMIT);
            query.setParameter("owner", ownerId);
    
            if (resourceServer != null) {
                query.setParameter("serverId", resourceServer.getId());
            }
    
            if (pagination) {
                query.setFirstResult(firstResult);
                query.setMaxResults(maxResult);
            }
    
            ResourceStore resourceStore = provider.getStoreFactory().getResourceStore();
            closing(query.getResultStream().map(id -> resourceStore.findById(resourceServer, id.getId()))).forEach(consumer);
        }

        private String getQueryName(ResourceServer resourceServer, boolean pagination) {
            if (resourceServer == null) {
                return pagination ? "findAnyResourceIdByOwnerOrdered" : "findAnyResourceIdByOwner";
            }
            return pagination ? "findResourceIdByOwnerOrdered" : "findResourceIdByOwner";
        }
}
