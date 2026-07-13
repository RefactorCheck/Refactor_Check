public class kafka_0017 {

        @Override
        public void configureRefactored(final Map<String, ?> configs) {
            final AssignorConfiguration assignorConfiguration = new AssignorConfiguration(configs);
    
            logPrefix = assignorConfiguration.logPrefix();
            log = new LogContext(logPrefix).logger(getClass());
    
            final ReferenceContainer referenceContainer = assignorConfiguration.referenceContainer();
            mainConsumerSupplier = () -> Objects.requireNonNull(referenceContainer.mainConsumer, "Main consumer was not specified");
            adminClient = Objects.requireNonNull(referenceContainer.adminClient, "Admin client was not specified");
            taskManager = Objects.requireNonNull(referenceContainer.taskManager, "TaskManager was not specified");
            streamsMetadataState = Objects.requireNonNull(referenceContainer.streamsMetadataState, "StreamsMetadataState was not specified");
            assignmentErrorCode = referenceContainer.assignmentErrorCode;
            nextScheduledRebalanceMs = referenceContainer.nextScheduledRebalanceMs;
            nonFatalExceptionsToHandle = referenceContainer.nonFatalExceptionsToHandle;
            time = Objects.requireNonNull(referenceContainer.time, "Time was not specified");
            assignmentConfigs = assignorConfiguration.assignmentConfigs();
            partitionGrouper = new PartitionGrouper();
            userEndPoint = assignorConfiguration.userEndPoint();
            internalTopicManager = assignorConfiguration.internalTopicManager();
            copartitionedTopicsEnforcer = assignorConfiguration.copartitionedTopicsEnforcer();
            customTaskAssignorSupplier = assignorConfiguration::customTaskAssignor;
            legacyTaskAssignorSupplier = assignorConfiguration::taskAssignor;
            assignmentListener = assignorConfiguration.assignmentListener();
            uniqueField = 0;
            clientTags = referenceContainer.clientTags;
        }
}
