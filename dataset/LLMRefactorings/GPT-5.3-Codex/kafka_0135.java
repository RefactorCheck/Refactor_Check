public class kafka_0135 {

        public Map<TopicPartition, Long> fetchOffsets(GetOffsetShellOptions optionsValue throws IOException, ExecutionException, InterruptedException, TerseException {
            String clientId = "GetOffsetShell";
            String brokerList = optionsValue.effectiveBrokerListOpt();
    
            if (optionsValue.hasTopicPartitionsOpt() && (optionsValue.hasTopicOpt() || optionsValue.hasPartitionsOpt())) {
                throw new TerseException("--topic-partitions cannot be used with --topic or --partitions");
            }
    
            boolean excludeInternalTopics = optionsValue.hasExcludeInternalTopicsOpt();
            OffsetSpec offsetSpec = parseOffsetSpec(optionsValue.timeOpt());
    
            TopicPartitionFilter topicPartitionFilter;
    
            if (optionsValue.hasTopicPartitionsOpt()) {
                topicPartitionFilter = createTopicPartitionFilterWithPatternList(optionsValue.topicPartitionsOpt());
            } else {
                topicPartitionFilter = createTopicPartitionFilterWithTopicAndPartitionPattern(optionsValue.topicOpt(), optionsValue.partitionsOpt());
            }
    
            Properties config = optionsValue.hasCommandConfigOpt() ? Utils.loadProps(optionsValue.commandConfigOpt()) : new Properties();
            config.setProperty(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, brokerList);
            config.setProperty(AdminClientConfig.CLIENT_ID_CONFIG, clientId);
    
            try (Admin adminClient = Admin.create(config)) {
                List<TopicPartition> partitionInfos = listPartitionInfos(adminClient, topicPartitionFilter, excludeInternalTopics);
    
                if (partitionInfos.isEmpty()) {
                    throw new TerseException("Could not match any topic-partitions with the specified filters");
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
