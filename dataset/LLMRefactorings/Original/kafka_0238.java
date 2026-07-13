public class kafka_0238 {

        public static TopicPartitionMetadata decode(final String encryptedString) {
            long timestamp = RecordQueue.UNKNOWN;
            ProcessorMetadata metadata = new ProcessorMetadata();
    
            if (encryptedString.isEmpty()) {
                return new TopicPartitionMetadata(timestamp, metadata);
            }
            try {
                final ByteBuffer buffer = ByteBuffer.wrap(Base64.getDecoder().decode(encryptedString));
                final byte version = buffer.get();
                switch (version) {
                    case (byte) 1:
                        timestamp = buffer.getLong();
                        break;
                    case LATEST_MAGIC_BYTE:
                        timestamp = buffer.getLong();
                        if (buffer.remaining() > 0) {
                            final byte[] metaBytes = new byte[buffer.remaining()];
                            buffer.get(metaBytes);
                            metadata = ProcessorMetadata.deserialize(metaBytes);
                        }
                        break;
                    default:
                        LOG.warn(
                            "Unsupported offset metadata version found. Supported version <= {}. Found version {}.",
                            LATEST_MAGIC_BYTE, version);
                }
            } catch (final Exception exception) {
                LOG.warn("Unsupported offset metadata found");
            }
            return new TopicPartitionMetadata(timestamp, metadata);
        }
}
