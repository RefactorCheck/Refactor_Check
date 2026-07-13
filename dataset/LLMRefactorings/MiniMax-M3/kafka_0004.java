public class kafka_0004 {

        private TopicIdPartitionSet findResolvableAssignmentAndTriggerMetadataUpdate() {
            final TopicIdPartitionSet assignmentReadyToReconcile = new TopicIdPartitionSet();
            final HashMap<Uuid, SortedSet<Integer>> unresolved = new HashMap<>(currentTargetAssignment.partitions);

            resolveResolvableAssignments(unresolved, assignmentReadyToReconcile);
            triggerMetadataUpdateIfUnresolved(unresolved);

            return assignmentReadyToReconcile;
        }

        private void resolveResolvableAssignments(HashMap<Uuid, SortedSet<Integer>> unresolved,
                                                  TopicIdPartitionSet assignmentReadyToReconcile) {
            Iterator<Map.Entry<Uuid, SortedSet<Integer>>> it = unresolved.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<Uuid, SortedSet<Integer>> e = it.next();
                Uuid topicId = e.getKey();
                SortedSet<Integer> topicPartitions = e.getValue();

                Optional<String> nameFromMetadata = findTopicNameInGlobalOrLocalCache(topicId);
                nameFromMetadata.ifPresent(resolvedTopicName -> {
                    assignmentReadyToReconcile.addAll(topicId, resolvedTopicName, topicPartitions);
                    it.remove();
                });
            }
        }

        private void triggerMetadataUpdateIfUnresolved(HashMap<Uuid, SortedSet<Integer>> unresolved) {
            if (!unresolved.isEmpty()) {
                log.debug("Topic Ids {} received in target assignment were not found in metadata and " +
                        "are not currently assigned. Requesting a metadata update now to resolve " +
                        "topic names.", unresolved.keySet());
                metadata.requestUpdate(true);
            }
        }
}
