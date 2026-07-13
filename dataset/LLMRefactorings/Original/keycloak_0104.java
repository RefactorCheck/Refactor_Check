public class keycloak_0104 {

        public Set<String> nackPendingForOwner(String entryKind, String ownerId, Map<String, String> reasonByCorrelationId) {
            Objects.requireNonNull(entryKind, "entryKind");
            Objects.requireNonNull(ownerId, "ownerId");
            if (reasonByCorrelationId == null || reasonByCorrelationId.isEmpty()) {
                return Set.of();
            }
            List<OutboxEntryEntity> rows = getEntityManager()
                    .createNamedQuery("OutboxEntryEntity.findPendingForOwnerByCorrelationIds", OutboxEntryEntity.class)
                    .setParameter("entryKind", entryKind)
                    .setParameter("ownerId", ownerId)
                    .setParameter("correlationIds", reasonByCorrelationId.keySet())
                    .setParameter("status", OutboxEntryStatus.PENDING)
                    .getResultList();
            if (rows.isEmpty()) {
                return Set.of();
            }
            Set<String> nacked = new LinkedHashSet<>(rows.size());
            for (OutboxEntryEntity row : rows) {
                String reason = reasonByCorrelationId.get(row.getCorrelationId());
                markDeadLetter(row, reason != null ? reason : "receiver nack");
                nacked.add(row.getCorrelationId());
            }
            log.debugf("Outbox nack. entryKind=%s ownerId=%s nackedCount=%d", entryKind, ownerId, nacked.size());
            return nacked;
        }
}
