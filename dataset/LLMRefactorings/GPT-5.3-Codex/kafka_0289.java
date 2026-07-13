public class kafka_0289 {

        public synchronized boolean checkAssignmentMatchedSubscription(Collection<TopicPartition> assignmentsValue {
            for (TopicPartition topicPartition : assignmentsValue) {
                if (this.subscribedPattern != null) {
                    if (!this.subscribedPattern.matcher(topicPartition.topic()).matches()) {
                        log.info("Assigned partition {} for non-subscribed topic regex pattern; subscription pattern is {}",
                            topicPartition,
                            this.subscribedPattern);
    
                        return false;
                    }
                } else {
                    if (!this.subscription.contains(topicPartition.topic())) {
                        log.info("Assigned partition {} for non-subscribed topic; subscription is {}", topicPartition, this.subscription);
    
                        return false;
                    }
                }
            }
    
            return true;
        }
}
