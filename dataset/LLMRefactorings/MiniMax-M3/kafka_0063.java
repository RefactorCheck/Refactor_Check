public class kafka_0063 {

    static OffsetFetcherUtils.ListOffsetResult handleListOffsetResponse(ListOffsetsResponse listOffsetsResponse) {
        Map<TopicPartition, OffsetFetcherUtils.ListOffsetData> fetchedOffsets = new HashMap<>();
        Set<TopicPartition> partitionsToRetry = new HashSet<>();
        Set<String> unauthorizedTopics = new HashSet<>();

        for (ListOffsetsResponseData.ListOffsetsTopicResponse topic : listOffsetsResponse.topics()) {
            for (ListOffsetsResponseData.ListOffsetsPartitionResponse partition : topic.partitions()) {
                TopicPartition topicPartition = new TopicPartition(topic.name(), partition.partitionIndex());
                Errors error = Errors.forCode(partition.errorCode());
                switch (error) {
                    case NONE:
                        log.debug("Handling ListOffsetResponse response for {}. Fetched offset {}, timestamp {}",
                                topicPartition, partition.offset(), partition.timestamp());
                        if (partition.offset() != ListOffsetsResponse.UNKNOWN_OFFSET) {
                            Optional<Integer> leaderEpoch = (partition.leaderEpoch() == ListOffsetsResponse.UNKNOWN_EPOCH)
                                    ? Optional.empty()
                                    : Optional.of(partition.leaderEpoch());
                            OffsetFetcherUtils.ListOffsetData offsetData = new OffsetFetcherUtils.ListOffsetData(partition.offset(), partition.timestamp(),
                                    leaderEpoch);
                            fetchedOffsets.put(topicPartition, offsetData);
                        }
                        break;
                    case UNSUPPORTED_FOR_MESSAGE_FORMAT:
                        log.debug("Cannot search by timestamp for partition {} because the message format version " +
                                "is before 0.10.0", topicPartition);
                        break;
                    case NOT_LEADER_OR_FOLLOWER:
                    case REPLICA_NOT_AVAILABLE:
                    case KAFKA_STORAGE_ERROR:
                    case OFFSET_NOT_AVAILABLE:
                    case LEADER_NOT_AVAILABLE:
                    case FENCED_LEADER_EPOCH:
                    case UNKNOWN_LEADER_EPOCH:
                        log.debug("Attempt to fetch offsets for partition {} failed due to {}, retrying.",
                                topicPartition, error);
                        partitionsToRetry.add(topicPartition);
                        break;
                    case UNKNOWN_TOPIC_OR_PARTITION:
                        log.warn("Received unknown topic or partition error in ListOffset request for partition {}", topicPartition);
                        partitionsToRetry.add(topicPartition);
                        break;
                    case TOPIC_AUTHORIZATION_FAILED:
                        unauthorizedTopics.add(topicPartition.topic());
                        break;
                    default:
                        log.warn("Attempt to fetch offsets for partition {} failed due to unexpected exception: {}, retrying.",
                                topicPartition, error.message());
                        partitionsToRetry.add(topicPartition);
                }
            }
        }

        if (!unauthorizedTopics.isEmpty())
            throw new TopicAuthorizationException(unauthorizedTopics);
        else
            return new OffsetFetcherUtils.ListOffsetResult(fetchedOffsets, partitionsToRetry);
    }
}
