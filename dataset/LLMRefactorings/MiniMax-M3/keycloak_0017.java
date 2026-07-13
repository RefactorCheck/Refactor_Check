public class keycloak_0017 {

        protected void processOne(KeycloakSession session, OutboxStore store, OutboxEntryEntity row) {
            OutboxDeliveryResult result;
            try {
                result = handler.deliver(session, row);
                if (result == null) {
                    result = OutboxDeliveryResult.retry("delivery handler returned null result");
                }
            } catch (RuntimeException e) {
                log.warnf(e, "Outbox delivery handler threw — treating as RETRY. id=%s entryKind=%s correlationId=%s",
                        row.getId(), row.getEntryKind(), row.getCorrelationId());
                String message = e.getMessage() == null
                        ? e.getClass().getSimpleName()
                        : e.getClass().getSimpleName() + ": " + e.getMessage();
                result = OutboxDeliveryResult.retry(message);
            }

            switch (result.outcome()) {
                case DELIVERED -> {
                    store.markDelivered(row);
                    log.debugf("Outbox delivered. id=%s entryKind=%s correlationId=%s attempts=%d",
                            row.getId(), row.getEntryKind(), row.getCorrelationId(), row.getAttempts());
                }
                case RETRY -> handleRetry(store, row, result.errorMessage());
                case DEAD_LETTER -> {
                    String reason = deadLetterReason(result, "handler returned DEAD_LETTER (attempt " + (row.getAttempts() + 1) + ")");
                    store.markDeadLetter(row, reason);
                    log.warnf("Outbox dead-lettered by handler. id=%s entryKind=%s correlationId=%s reason=%s",
                            row.getId(), row.getEntryKind(), row.getCorrelationId(), reason);
                }
                case ORPHANED -> {
                    String reason = deadLetterReason(result, "handler returned ORPHANED (destination no longer exists)");
                    store.markDeadLetter(row, reason);
                    log.warnf("Outbox dead-lettered as orphan. id=%s entryKind=%s correlationId=%s",
                            row.getId(), row.getEntryKind(), row.getCorrelationId());
                }
            }
        }

        private String deadLetterReason(OutboxDeliveryResult result, String defaultReason) {
            String errorMessage = result.errorMessage();
            return errorMessage != null ? errorMessage : defaultReason;
        }
}
