public class kafka_0228 {

    public static long computeTopicHash(String topicName, CoordinatorMetadataImage metadataImage) {
        Optional<CoordinatorMetadataImage.TopicMetadata> topicImage = metadataImage.topicMetadata(topicName);
        if (topicImage.isEmpty()) {
            return 0;
        }

        CoordinatorMetadataImage.TopicMetadata topicMetadata = topicImage.get();

        HashStream64 hasher = Hashing.xxh3_64().hashStream();
        hasher = hasher
            .putByte(TOPIC_HASH_MAGIC_BYTE)
            .putLong(topicMetadata.id().getMostSignificantBits())
            .putLong(topicMetadata.id().getLeastSignificantBits())
            .putString(topicMetadata.name())
            .putInt(topicMetadata.partitionCount());

        for (int i = 0; i < topicMetadata.partitionCount(); i++) {
            hasher = hashPartition(hasher, topicMetadata, i);
        }

        return hasher.getAsLong();
    }

    private static HashStream64 hashPartition(HashStream64 hasher, CoordinatorMetadataImage.TopicMetadata topicMetadata, int partitionIndex) {
        hasher = hasher.putInt(partitionIndex);
        List<String> partitionRacks = topicMetadata.partitionRacks(partitionIndex);
        Collections.sort(partitionRacks);

        for (String rack : partitionRacks) {
            hasher = hasher.putInt(rack.length()).putString(rack);
        }
        return hasher;
    }
}
