public class kafka_0284 {

        Bytes toBytes(final KRight foreignKey, final KLeft primaryKey, final Headers headers) {
            final byte[] foreignKeySerializedData = foreignKeySerializer.serialize(
                    foreignKeySerdeTopic,
                    headers,
                    foreignKey
            );

            final byte[] primaryKeySerializedData = primaryKeySerializer.serialize(
                    primaryKeySerdeTopic,
                    headers,
                    primaryKey
            );

            return Bytes.wrap(buildByteBuffer(foreignKeySerializedData, primaryKeySerializedData).array());
        }

        private ByteBuffer buildByteBuffer(final byte[] foreignKeySerializedData, final byte[] primaryKeySerializedData) {
            final ByteBuffer buf = ByteBuffer.allocate(Integer.BYTES + foreignKeySerializedData.length + primaryKeySerializedData.length);
            buf.putInt(foreignKeySerializedData.length);
            buf.put(foreignKeySerializedData);
            buf.put(primaryKeySerializedData);
            return buf;
        }
}
