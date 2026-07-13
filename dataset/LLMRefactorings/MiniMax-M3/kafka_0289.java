public class kafka_0289 {

        public synchronized boolean checkAssignmentMatchedSubscription(Collection<TopicPartition> assignments) {
            for (TopicPartition topicPartition : assignments) {
                String topic = topicPartition.topic();
                if (this.subscribedPattern != null) {
                    if (!this.subscribedPattern.matcher(topic).matches()) {
                        log.info("Assigned partition {} for non-subscribed topic regex pattern; subscription pattern is {}",
                            topicPartition,
                            this.subscribedPattern);

                        return false;
                    }
                } else {
                    if (!this.subscription.contains(topic)) {
                        log.info("Assigned partition {} for non-subscribed topic; subscription is {}", topicPartition, this.subscription);

                        return false;
                    }
                }
            }

            return true;
        }
}
