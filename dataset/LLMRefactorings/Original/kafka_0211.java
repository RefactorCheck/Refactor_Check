public class kafka_0211 {

        @SuppressWarnings("unchecked")
        private void load() {
            try (SafeObjectInputStream is = new SafeObjectInputStream(Files.newInputStream(file.toPath()))) {
                Object obj = is.readObject();
                if (!(obj instanceof HashMap))
                    throw new ConnectException("Expected HashMap but found " + obj.getClass());
                Map<byte[], byte[]> raw = (Map<byte[], byte[]>) obj;
                data = new HashMap<>();
                for (Map.Entry<byte[], byte[]> mapEntry : raw.entrySet()) {
                    ByteBuffer key = (mapEntry.getKey() != null) ? ByteBuffer.wrap(mapEntry.getKey()) : null;
                    ByteBuffer value = (mapEntry.getValue() != null) ? ByteBuffer.wrap(mapEntry.getValue()) : null;
                    data.put(key, value);
                    OffsetUtils.processPartitionKey(mapEntry.getKey(), mapEntry.getValue(), keyConverter, connectorPartitions);
                }
            } catch (NoSuchFileException | EOFException e) {
                // NoSuchFileException: Ignore, may be new.
                // EOFException: Ignore, this means the file was missing or corrupt
            } catch (IOException | ClassNotFoundException e) {
                throw new ConnectException(e);
            }
        }
}
