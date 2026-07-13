public class keycloak_0007 {

        private void findByOwnerFilter(String ownerId, ResourceServer resourceServer, Consumer<Resource> consumer, int firstResult, int maxResult) {
            boolean hasPagination = firstResult > -1 && maxResult > -1;
            String namedQuery = hasPagination ? "findResourceIdByOwnerOrdered" : "findResourceIdByOwner";

            if (resourceServer == null) {
                namedQuery = hasPagination ? "findAnyResourceIdByOwnerOrdered" : "findAnyResourceIdByOwner";
            }

            TypedQuery<ResourceEntity> query = entityManager.createNamedQuery(namedQuery, ResourceEntity.class);

            query.setFlushMode(FlushModeType.COMMIT);
            query.setParameter("owner", ownerId);

            if (resourceServer != null) {
                query.setParameter("serverId", resourceServer.getId());
            }

            if (hasPagination) {
                query.setFirstResult(firstResult);
                query.setMaxResults(maxResult);
            }

            ResourceStore resourceStore = provider.getStoreFactory().getResourceStore();
            closing(query.getResultStream().map(id -> resourceStore.findById(resourceServer, id.getId()))).forEach(consumer);
        }
}
