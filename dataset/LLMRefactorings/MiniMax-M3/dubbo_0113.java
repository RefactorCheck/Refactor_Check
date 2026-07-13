public class dubbo_0113 {

            public byte[] toByteArray() {
                int totalSize = 0;

                byte[] serializeTypeEncoded = encodeStringField(serializeType, 1);
                totalSize += serializeTypeEncoded.length;

                int dataTag = makeTag(2, 2);
                if (data != null) {
                    totalSize += varIntComputeLength(dataTag) + varIntComputeLength(data.length) + data.length;
                }

                byte[] typeEncoded = encodeStringField(type, 3);
                totalSize += typeEncoded.length;

                ByteBuffer byteBuffer = ByteBuffer.allocate(totalSize);
                byteBuffer.put(serializeTypeEncoded);
                if (data != null) {
                    byteBuffer
                            .put(varIntEncode(dataTag))
                            .put(varIntEncode(data.length))
                            .put(data);
                }
                byteBuffer.put(typeEncoded);
                return byteBuffer.array();
            }

            private byte[] encodeStringField(String value, int tagNumber) {
                int tag = makeTag(tagNumber, 2);
                byte[] tagBytes = varIntEncode(tag);
                byte[] valueBytes = value.getBytes(StandardCharsets.UTF_8);
                byte[] lengthBytes = varIntEncode(valueBytes.length);
                byte[] result = new byte[tagBytes.length + lengthBytes.length + valueBytes.length];
                ByteBuffer.wrap(result).put(tagBytes).put(lengthBytes).put(valueBytes);
                return result;
            }
}
