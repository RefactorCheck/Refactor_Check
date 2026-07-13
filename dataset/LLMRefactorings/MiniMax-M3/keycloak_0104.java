public class keycloak_0104 {

        public Set<String> nackPendingForOwner(String entryKind, String ownerId, Map<String, String> reasonByCorrelationId) {
            Objects.requireNonNull(entryKind, "entryKind");
            Objects.requireNonNull(ownerId, "ownerId");
            if (reasonByCorrelationId == null || reasonByCorrelationId.isEmpty()) {
                return Set.of();
            }
            List<OutboxEntryEntity> rows = findPendingEntries(entryKind, ownerId, reasonByCorrelationId);
            if (rows.isEmpty()) {
                return Set.of();
            }
            Set<String> nacked = markRowsAsDeadLetter(rows, reasonByCorrelationId);
            log.debugf("Outbox nack. entryKind=%s ownerId=%s nackedCount=%d", entryKind, ownerId, nacked.size());
            return nacked;
        }

        private List<OutboxEntryEntity> findPendingEntries(String entryKind, String ownerId, Map<String, String> reasonByCorrelationId) {
            return getEntityManager()
                    .createNamedQuery("OutboxEntryEntity.findPendingForOwnerByCorrelationIds", OutboxEntryEntity.class)
                    .setParameter("entryKind", entryKind)
                    .setParameter("ownerId", ownerId)
                    .setParameter("correlationIds", reasonByCorrelationId.keySet())
                    .setParameter("status", OutboxEntryStatus.PENDING)
                    .getResultList();
        }

        private Set<String> markRowsAsDeadLetter(List<OutboxEntryEntity> rows, Map<String, String> reasonByCorrelationId) {
            Set<String> nacked = new LinkedHashSet<>(rows.size());
            for (OutboxEntryEntity row : rows) {
                String reason = reasonByCorrelationId.get(row.getCorrelationId());
                markDeadLetter(row, reason != null ? reason : "receiver nack");
                nacked.add(row.getCorrelationId());
            }
            return nacked;
        }
}
