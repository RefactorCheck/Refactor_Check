public class keycloak_0085 {

        public List<OutboxEntryEntity> lockPendingForOwner(String entryKind, String ownerId, int limit) {
            Objects.requireNonNull(entryKind, "entryKind");
            Objects.requireNonNull(ownerId, "ownerId");
            if (limit <= 0) {
                throw new IllegalArgumentException("limit must be positive, got " + limit);
            }
            String queryName = "OutboxEntryEntity.findPendingForOwner";
            var query = getEntityManager()
                    .createNamedQuery(queryName, OutboxEntryEntity.class)
                    .setParameter("entryKind", entryKind)
                    .setParameter("ownerId", ownerId)
                    .setParameter("status", OutboxEntryStatus.PENDING)
                    .setMaxResults(limit)
                    .setLockMode(LockModeType.PESSIMISTIC_WRITE);
            try {
                query.unwrap(SelectionQuery.class).setHibernateLockMode(LockMode.UPGRADE_SKIPLOCKED);
            } catch (RuntimeException e) {
                log.debugf(e, "Could not set UPGRADE_SKIPLOCKED on owner-pending query — proceeding without skip-locked");
            }
            return query.getResultList();
        }
}
