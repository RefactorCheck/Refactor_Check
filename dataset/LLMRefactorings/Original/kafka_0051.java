public class kafka_0051 {

        @Override
        protected JoinGroupRequestData.JoinGroupRequestProtocolCollection metadata() {
            log.debug("Joining group with current subscription: {}", subscriptions.subscription());
            this.joinedSubscription = subscriptions.subscription();
            JoinGroupRequestData.JoinGroupRequestProtocolCollection protocolSet = new JoinGroupRequestData.JoinGroupRequestProtocolCollection();
    
            List<String> topics = new ArrayList<>(joinedSubscription);
            for (ConsumerPartitionAssignor assignor : assignors) {
                Subscription subscription = new Subscription(topics,
                                                             assignor.subscriptionUserData(joinedSubscription),
                                                             subscriptions.assignedPartitionsList(),
                                                             generation().generationId,
                                                             rackId);
                ByteBuffer metadata = ConsumerProtocol.serializeSubscription(subscription);
    
                protocolSet.add(new JoinGroupRequestData.JoinGroupRequestProtocol()
                        .setName(assignor.name())
                        .setMetadata(Utils.toArray(metadata)));
            }
            return protocolSet;
        }
}
