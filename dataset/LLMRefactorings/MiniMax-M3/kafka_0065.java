public class kafka_0065 {

    private void assignCoPartitionedWithRackMatching(LinkedHashMap<String, Optional<String>> consumers,
                                                     int numPartitions,
                                                     Collection<TopicAssignmentState> assignmentStates,
                                                     Map<String, List<TopicPartition>> assignment) {

        Set<String> remainingConsumers = new LinkedHashSet<>(consumers.keySet());
        for (int i = 0; i < numPartitions; i++) {
            int p = i;

            Optional<String> matchingConsumer = findMatchingConsumer(remainingConsumers, assignmentStates, p);
            if (matchingConsumer.isPresent()) {
                String consumer = matchingConsumer.get();
                assignmentStates.forEach(t -> assign(consumer, Collections.singletonList(new TopicPartition(t.topic, p)), t, assignment));

                if (assignmentStates.stream().noneMatch(t -> t.maxAssignable(consumer) > 0)) {
                    remainingConsumers.remove(consumer);
                    if (remainingConsumers.isEmpty())
                        break;
                }
            }
        }
    }

    private Optional<String> findMatchingConsumer(Set<String> remainingConsumers,
                                                 Collection<TopicAssignmentState> assignmentStates,
                                                 int partition) {
        return remainingConsumers.stream()
                .filter(c -> assignmentStates.stream().allMatch(t -> t.racksMatch(c, new TopicPartition(t.topic, partition)) && t.maxAssignable(c) > 0))
                .findFirst();
    }
}
