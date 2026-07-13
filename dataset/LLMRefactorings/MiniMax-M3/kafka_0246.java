public class kafka_0246 {

    @Override
    public ByteBuffer subscriptionUserData(final Set<String> topics) {
        // Adds the following information to subscription
        // 1. Client ProcessId (a UUID assigned to an instance of KafkaStreams)
        // 2. Map from task id to its overall lag
        // 3. Unique Field to ensure a rebalance when a thread rejoins by forcing the user data to be different

        handleRebalanceStart(topics);
        uniqueField++;

        final Map<TaskId, Long> taskOffsetSums = computeTaskOffsetSums();

        return new SubscriptionInfo(
            usedSubscriptionMetadataVersion,
            LATEST_SUPPORTED_VERSION,
            taskManager.processId(),
            userEndPoint,
            taskOffsetSums,
            uniqueField,
            assignmentErrorCode.get(),
            clientTags
        ).encode();
    }

    private Map<TaskId, Long> computeTaskOffsetSums() {
        final Set<String> currentNamedTopologies = taskManager.topologyMetadata().namedTopologiesView();

        // If using NamedTopologies, filter out any that are no longer recognized/have been removed
        return taskManager.topologyMetadata().hasNamedTopologies() ?
            filterMap(taskManager.taskOffsetSums(), t -> currentNamedTopologies.contains(t.getKey().topologyName())) :
            taskManager.taskOffsetSums();
    }
}
