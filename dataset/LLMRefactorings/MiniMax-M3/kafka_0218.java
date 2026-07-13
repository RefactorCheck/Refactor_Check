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
                nullIndexList = deserializeNullIndexList(dis, data.length);
            }
            final int size = readListSize(dis, data.length);
            List<Inner> deserializedList = createListInstance(size);
            for (int i = 0; i < size; i++) {
                int entrySize = readEntrySize(dis, serStrategy, data.length);
                if (entrySize == ListSerde.NULL_ENTRY_VALUE || (nullIndexList != null && nullIndexList.contains(i))) {
                    deserializedList.add(null);
                    continue;
                }
                deserializedList.add(readEntry(dis, entrySize, topic, headers, deserializedList));
            }
            return deserializedList;
        } catch (IOException e) {
            throw new KafkaException("Unable to deserialize into a List", e);
        }
    }

    private Inner readEntry(DataInputStream dis, int entrySize, String topic, Headers headers, List<Inner> deserializedList) throws IOException {
        byte[] payload = new byte[entrySize];
        if (dis.read(payload) == -1) {
            log.error("Ran out of bytes in serialized list");
            log.trace("Deserialized list so far: {}", deserializedList);
            throw new SerializationException("End of the stream was reached prematurely");
        }
        return inner.deserialize(topic, headers, payload);
    }
}
