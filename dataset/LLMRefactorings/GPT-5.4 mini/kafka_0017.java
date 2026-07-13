public class kafka_0017 {

        @Override
        public void configure(final Map<String, ?> configs) {
            final AssignorConfiguration assignorConfiguration = new AssignorConfiguration(configs);

            logPrefix = assignorConfiguration.logPrefix();
            log = new LogContext(logPrefix).logger(getClass());

            final ReferenceContainer container = assignorConfiguration.referenceContainer();
            mainConsumerSupplier = () -> Objects.requireNonNull(container.mainConsumer, "Main consumer was not specified");
            adminClient = Objects.requireNonNull(container.adminClient, "Admin client was not specified");
            taskManager = Objects.requireNonNull(container.taskManager, "TaskManager was not specified");
            streamsMetadataState = Objects.requireNonNull(container.streamsMetadataState, "StreamsMetadataState was not specified");
            assignmentErrorCode = container.assignmentErrorCode;
            nextScheduledRebalanceMs = container.nextScheduledRebalanceMs;
            nonFatalExceptionsToHandle = container.nonFatalExceptionsToHandle;
            time = Objects.requireNonNull(container.time, "Time was not specified");
            assignmentConfigs = assignorConfiguration.assignmentConfigs();
            partitionGrouper = new PartitionGrouper();
            userEndPoint = assignorConfiguration.userEndPoint();
            internalTopicManager = assignorConfiguration.internalTopicManager();
            copartitionedTopicsEnforcer = assignorConfiguration.copartitionedTopicsEnforcer();
            customTaskAssignorSupplier = assignorConfiguration::customTaskAssignor;
            legacyTaskAssignorSupplier = assignorConfiguration::taskAssignor;
            assignmentListener = assignorConfiguration.assignmentListener();
            uniqueField = 0;
            clientTags = container.clientTags;
        }
}
