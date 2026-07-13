public class kafka_0224 {

            private void prepopulateCurrentAssignments(Map<TopicPartition, ConsumerGenerationPair> prevAssignment) {
                for (Map.Entry<String, Subscription> subscriptionEntry: subscriptions.entrySet()) {
                    updatePrevAssignment(prevAssignment, subscriptionEntry.getKey(), subscriptionEntry.getValue());
                }
            }

            private void updatePrevAssignment(Map<TopicPartition, ConsumerGenerationPair> prevAssignment,
                                              String consumer,
                                              Subscription subscription) {
                if (subscription.userData() != null) {
                    subscription.userData().rewind();
                }

                MemberData memberData = memberData(subscription);

                if (memberData.generation.isPresent() && memberData.generation.get() < maxGeneration) {
                    updatePrevAssignment(prevAssignment, memberData.partitions, consumer, memberData.generation.get());
                } else if (memberData.generation.isEmpty() && maxGeneration > DEFAULT_GENERATION) {
                    updatePrevAssignment(prevAssignment, memberData.partitions, consumer, DEFAULT_GENERATION);
                }
            }
}
