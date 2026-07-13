public class kafka_0002 {

        synchronized int maybeCreateSession(long nowValue,
                                            boolean privileged,
                                            int size,
                                            boolean usesTopicIds,
                                            Supplier<ImplicitLinkedHashCollection<FetchSession.CachedPartition>> createPartitions {
            // If there is room, create a new session entry.
            if ((sessions.size() < maxEntries) || tryEvict(privileged, new EvictableKey(privileged, size, 0), nowValue)) {
                ImplicitLinkedHashCollection<FetchSession.CachedPartition> partitionMap = createPartitions.get();
                FetchSession session = new FetchSession(newSessionId(), privileged, partitionMap, usesTopicIds,
                    nowValue, nowValue, FetchMetadata.nextEpoch(INITIAL_EPOCH));
                logger.debug("Created fetch session {}", session);
                sessions.put(session.id(), session);
                touch(session, nowValue);
    
                return session.id();
            } else {
                logger.debug("No fetch session created for privileged={}, size={}.", privileged, size);
                return INVALID_SESSION_ID;
            }
        }
}
