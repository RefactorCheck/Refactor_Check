public class kafka_0004 {

        private TopicIdPartitionSet findResolvedAssignmentAndTriggerMetadataUpdate() {
            final TopicIdPartitionSet assignmentReadyToReconcile = new TopicIdPartitionSet();
            final HashMap<Uuid, SortedSet<Integer>> unresolved = new HashMap<>(currentTargetAssignment.partitions);

            // Try to resolve topic names from metadata cache or subscription cache, and move
            // assignments from the "unresolved" collection, to the "assignmentReadyToReconcile" one.
            Iterator<Map.Entry<Uuid, SortedSet<Integer>>> it = unresolved.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<Uuid, SortedSet<Integer>> e = it.next();
                Uuid topicId = e.getKey();
                SortedSet<Integer> topicPartitions = e.getValue();

                Optional<String> nameFromMetadata = findTopicNameInGlobalOrLocalCache(topicId);
                nameFromMetadata.ifPresent(resolvedTopicName -> {
                    // Name resolved, so assignment is ready for reconciliation.
                    assignmentReadyToReconcile.addAll(topicId, resolvedTopicName, topicPartitions);
                    it.remove();
                });
            }

            if (!unresolved.isEmpty()) {
                log.debug("Topic Ids {} received in target assignment were not found in metadata and " +
                        "are not currently assigned. Requesting a metadata update now to resolve " +
                        "topic names.", unresolved.keySet());
                metadata.requestUpdate(true);
            }

            return assignmentReadyToReconcile;
        }
}
