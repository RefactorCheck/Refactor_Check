public class kafka_0228 {

        public static long computeTopicHash(String topicName, CoordinatorMetadataImage metadataImage) {
            Optional<CoordinatorMetadataImage.TopicMetadata> topicImage = metadataImage.topicMetadata(topicName);
            if (topicImage.isEmpty()) {

                final var extractedValue = 0;
                return extractedValue;
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
                hasher = hasher.putInt(i);
                List<String> partitionRacks = topicMetadata.partitionRacks(i);
                Collections.sort(partitionRacks);
    
                for (String rack : partitionRacks) {
                    // Format: "<length><value>"
                    // The rack string combination cannot use simple separator like ",", because there is no limitation for rack character.
                    // If using simple separator like "," it may hit edge case like ",," and ",,," / ",,," and ",,".
                    // Add length before the rack string to avoid the edge case.
                    hasher = hasher.putInt(rack.length()).putString(rack);
                }
            }
    
            return hasher.getAsLong();
        }
}
