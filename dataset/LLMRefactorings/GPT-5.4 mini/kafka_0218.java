public class kafka_0218 {

        @Override
        public List<Inner> deserialize(String topic, Headers headers, byte[] data) {
            if (data == null) {
                return null;
            }
            try (final DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data))) {
                SerializationStrategy serStrategy = parseSerializationStrategyFlag(dis.readByte());
                List<Integer> nullIndexList = null;
                if (serStrategy == SerializationStrategy.CONSTANT_SIZE) {
                    if (primitiveSize == null) {
                        throw new SerializationException("Data is encoded as constant size entries, but configured inner deserializer is not a known fixed-size deserializer.");
                    }
                    // In CONSTANT_SIZE strategy, indexes of null entries are decoded from a null index list
                    nullIndexList = deserializeNullIndexList(dis, data.length);
                }
                final int size = readListSize(dis, data.length);
                List<Inner> deserializedList = createListInstance(size);
                for (int i = 0; i < size; i++) {
                    int entrySize = readEntrySize(dis, serStrategy, data.length);
                    boolean nullEntry = entrySize == ListSerde.NULL_ENTRY_VALUE || (nullIndexList != null && nullIndexList.contains(i));
                    if (nullEntry) {
                        deserializedList.add(null);
                        continue;
                    }
                    byte[] payload = new byte[entrySize];
                    if (dis.read(payload) == -1) {
                        log.error("Ran out of bytes in serialized list");
                        log.trace("Deserialized list so far: {}", deserializedList); // avoid logging actual data above TRACE level since it may contain sensitive information
                        throw new SerializationException("End of the stream was reached prematurely");
                    }
                    deserializedList.add(inner.deserialize(topic, headers, payload));
                }
                return deserializedList;
            } catch (IOException e) {
                throw new KafkaException("Unable to deserialize into a List", e);
            }
        }
}
