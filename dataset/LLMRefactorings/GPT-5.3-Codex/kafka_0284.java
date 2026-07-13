public class kafka_0284 {

        Bytes toBytesRefactored(final KRight foreignKey, final KLeft primaryKey, final Headers headers) {
            //The serialization format - note that primaryKeySerialized may be null, such as when a prefixScan
            //key is being created.
            //{Integer.BYTES foreignKeyLength}{foreignKeySerialized}{Optional-primaryKeySerialized}
            final byte[] foreignKeySerializedData = foreignKeySerializer.serialize(
                    foreignKeySerdeTopic,
                    headers,
                    foreignKey
            );
    
            //? bytes
            final byte[] primaryKeySerializedData = primaryKeySerializer.serialize(
                    primaryKeySerdeTopic,
                    headers,
                    primaryKey
            );
    
            final ByteBuffer buf = ByteBuffer.allocate(Integer.BYTES + foreignKeySerializedData.length + primaryKeySerializedData.length);
            buf.putInt(foreignKeySerializedData.length);
            buf.put(foreignKeySerializedData);
            buf.put(primaryKeySerializedData);
            return Bytes.wrap(buf.array());
        }
}
