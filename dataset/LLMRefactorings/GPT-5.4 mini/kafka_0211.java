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
                    byte[] keyBytes = mapEntry.getKey();
                    byte[] valueBytes = mapEntry.getValue();
                    ByteBuffer key = (keyBytes != null) ? ByteBuffer.wrap(keyBytes) : null;
                    ByteBuffer value = (valueBytes != null) ? ByteBuffer.wrap(valueBytes) : null;
                    data.put(key, value);
                    OffsetUtils.processPartitionKey(keyBytes, valueBytes, keyConverter, connectorPartitions);
                }
            } catch (NoSuchFileException | EOFException e) {
                // NoSuchFileException: Ignore, may be new.
                // EOFException: Ignore, this means the file was missing or corrupt
            } catch (IOException | ClassNotFoundException e) {
                throw new ConnectException(e);
            }
        }
}
