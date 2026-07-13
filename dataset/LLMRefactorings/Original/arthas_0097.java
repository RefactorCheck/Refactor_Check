public class arthas_0097 {

        @Override
        public void cleanupOldEvents(String sessionId, long maxAge) {
            List<StoredEvent> events = sessionEvents.get(sessionId);
            if (events == null || events.isEmpty()) {
                return;
            }
            
            Instant cutoff = Instant.now().minusMillis(maxAge);
            
            List<StoredEvent> toRemove = events.stream()
                .filter(event -> event.getTimestamp().isBefore(cutoff))
                .collect(Collectors.toList());
            
            for (StoredEvent event : toRemove) {
                events.remove(event);
                eventIdToSession.remove(event.getEventId());
            }
            
            if (!toRemove.isEmpty()) {
                logger.debug("Cleaned up {} old events for session {}", toRemove.size(), sessionId);
            }
        }
}
