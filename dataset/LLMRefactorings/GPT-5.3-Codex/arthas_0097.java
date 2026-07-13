public class arthas_0097 {
            private static final String CLEANUPOLDEVENTS_VALUE = "Cleaned up {} old events for session {}";


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
                logger.debug(CLEANUPOLDEVENTS_VALUE, toRemove.size(), sessionId);
            }
        }
}
