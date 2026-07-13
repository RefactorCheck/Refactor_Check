public class kafka_0224 {

            private void prepopulateCurrentAssignments(Map<TopicPartition, ConsumerGenerationPair> prevAssignment) {
                // we need to process subscriptions' user data with each consumer's reported generation in mind
                // higher generations overwrite lower generations in case of a conflict
                // note that a conflict could exist only if user data is for different generations

                for (Map.Entry<String, Subscription> subscriptionEntry: subscriptions.entrySet()) {
                    processSubscription(subscriptionEntry, prevAssignment);
                }
            }

            private void processSubscription(Map.Entry<String, Subscription> subscriptionEntry, Map<TopicPartition, ConsumerGenerationPair> prevAssignment) {
                String consumer = subscriptionEntry.getKey();
                Subscription subscription = subscriptionEntry.getValue();
                if (subscription.userData() != null) {
                    // since this is our 2nd time to deserialize memberData, rewind userData is necessary
                    subscription.userData().rewind();
                }

                MemberData memberData = memberData(subscription);

                // we already have the maxGeneration info, so just compare the current generation of memberData, and put into prevAssignment
                if (memberData.generation.isPresent() && memberData.generation.get() < maxGeneration) {
                    // if the current member's generation is lower than maxGeneration, put into prevAssignment if needed
                    updatePrevAssignment(prevAssignment, memberData.partitions, consumer, memberData.generation.get());
                } else if (memberData.generation.isEmpty() && maxGeneration > DEFAULT_GENERATION) {
                    // if maxGeneration is larger than DEFAULT_GENERATION
                    // put all (no generation) partitions as DEFAULT_GENERATION into prevAssignment if needed
                    updatePrevAssignment(prevAssignment, memberData.partitions, consumer, DEFAULT_GENERATION);
                }
            }
}
