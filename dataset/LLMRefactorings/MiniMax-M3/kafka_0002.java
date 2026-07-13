public class kafka_0002 {

        synchronized int maybeCreateSession(long now,
                                            boolean privileged,
                                            int size,
                                            boolean usesTopicIds,
                                            Supplier<ImplicitLinkedHashCollection<FetchSession.CachedPartition>> createPartitions) {
            if ((sessions.size() < maxEntries) || tryEvict(privileged, new EvictableKey(privileged, size, 0), now)) {
                FetchSession session = createFetchSession(now, privileged, usesTopicIds, createPartitions);
                logger.debug("Created fetch session {}", session);
                sessions.put(session.id(), session);
                touch(session, now);

                return session.id();
            } else {
                logger.debug("No fetch session created for privileged={}, size={}.", privileged, size);
                return INVALID_SESSION_ID;
            }
        }

        private FetchSession createFetchSession(long now,
                                                boolean privileged,
                                                boolean usesTopicIds,
                                                Supplier<ImplicitLinkedHashCollection<FetchSession.CachedPartition>> createPartitions) {
            ImplicitLinkedHashCollection<FetchSession.CachedPartition> partitionMap = createPartitions.get();
            return new FetchSession(newSessionId(), privileged, partitionMap, usesTopicIds,
                now, now, FetchMetadata.nextEpoch(INITIAL_EPOCH));
        }
}
