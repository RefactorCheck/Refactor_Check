public class kafka_0228 {

        public static long computeTopicHash(String topicName, CoordinatorMetadataImage metadataImage) {
            Optional<CoordinatorMetadataImage.TopicMetadata> topicImage = metadataImage.topicMetadata(topicName);
            if (topicImage.isEmpty()) {
                return 0;
            }

            CoordinatorMetadataImage.TopicMetadata topicMetadata = topicImage.get();
            int partitionCount = topicMetadata.partitionCount();

            HashStream64 hasher = Hashing.xxh3_64().hashStream();
            hasher = hasher
                .putByte(TOPIC_HASH_MAGIC_BYTE)
                .putLong(topicMetadata.id().getMostSignificantBits())
                .putLong(topicMetadata.id().getLeastSignificantBits())
                .putString(topicMetadata.name())
                .putInt(partitionCount);

            for (int i = 0; i < partitionCount; i++) {
                hasher = hasher.putInt(i);
                List<String> partitionRacks = topicMetadata.partitionRacks(i);
                Collections.sort(partitionRacks);

                for (String rack : partitionRacks) {
                    hasher = hasher.putInt(rack.length()).putString(rack);
                }
            }

            return hasher.getAsLong();
        }
}
