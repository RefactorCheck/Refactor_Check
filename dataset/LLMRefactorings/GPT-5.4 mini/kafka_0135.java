public class kafka_0135 {

        private static final String NO_MATCHING_TOPIC_PARTITIONS_MESSAGE = "Could not match any topic-partitions with the specified filters";

        public Map<TopicPartition, Long> fetchOffsets(GetOffsetShellOptions options) throws IOException, ExecutionException, InterruptedException, TerseException {
            String clientId = "GetOffsetShell";
            String brokerList = options.effectiveBrokerListOpt();
    
            if (options.hasTopicPartitionsOpt() && (options.hasTopicOpt() || options.hasPartitionsOpt())) {
                throw new TerseException("--topic-partitions cannot be used with --topic or --partitions");
            }
    
            boolean excludeInternalTopics = options.hasExcludeInternalTopicsOpt();
            OffsetSpec offsetSpec = parseOffsetSpec(options.timeOpt());
    
            TopicPartitionFilter topicPartitionFilter;
    
            if (options.hasTopicPartitionsOpt()) {
                topicPartitionFilter = createTopicPartitionFilterWithPatternList(options.topicPartitionsOpt());
            } else {
                topicPartitionFilter = createTopicPartitionFilterWithTopicAndPartitionPattern(options.topicOpt(), options.partitionsOpt());
            }
    
            Properties config = options.hasCommandConfigOpt() ? Utils.loadProps(options.commandConfigOpt()) : new Properties();
            config.setProperty(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, brokerList);
            config.setProperty(AdminClientConfig.CLIENT_ID_CONFIG, clientId);
    
            try (Admin adminClient = Admin.create(config)) {
                List<TopicPartition> partitionInfos = listPartitionInfos(adminClient, topicPartitionFilter, excludeInternalTopics);
    
                if (partitionInfos.isEmpty()) {
                    throw new TerseException(NO_MATCHING_TOPIC_PARTITIONS_MESSAGE);
                }
    
                Map<TopicPartition, OffsetSpec> timestampsToSearch = partitionInfos.stream().collect(Collectors.toMap(tp -> tp, tp -> offsetSpec));
    
                ListOffsetsResult listOffsetsResult = adminClient.listOffsets(timestampsToSearch);
    
                TreeMap<TopicPartition, Long> partitionOffsets = new TreeMap<>(Comparator.comparing(TopicPartition::toString));
    
                for (TopicPartition partition : partitionInfos) {
                    ListOffsetsResultInfo partitionInfo;
    
                    try {
                        partitionInfo = listOffsetsResult.partitionResult(partition).get();
                    } catch (ExecutionException e) {
                        if (e.getCause() instanceof KafkaException) {
                            System.err.println("Skip getting offsets for topic-partition " + partition.toString() + " due to error: " + e.getMessage());
                        } else {
                            throw e;
                        }
    
                        continue;
                    }
    
                    if (partitionInfo.offset() != ListOffsetsResponse.UNKNOWN_OFFSET) {
                        partitionOffsets.put(partition, partitionInfo.offset());
                    }
                }
    
                return partitionOffsets;
            }
        }
}
