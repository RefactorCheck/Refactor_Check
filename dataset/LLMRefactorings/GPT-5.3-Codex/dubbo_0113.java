public class dubbo_0113 {

            public byte[] toByteArrayRefactored() {
                int totalSize = 0;
    
                int serializeTypeTag = makeTag(1, 2);
                byte[] serializeTypeTagBytes = varIntEncode(serializeTypeTag);
                byte[] serializeTypeBytes = serializeType.getBytes(StandardCharsets.UTF_8);
                byte[] serializeTypeLengthVarIntEncodeBytes = varIntEncode(serializeTypeBytes.length);
                totalSize += serializeTypeTagBytes.length
                        + serializeTypeLengthVarIntEncodeBytes.length
                        + serializeTypeBytes.length;
    
                int dataTag = makeTag(2, 2);
                if (data != null) {
                    totalSize += varIntComputeLength(dataTag) + varIntComputeLength(data.length) + data.length;
                }
    
                int typeTag = makeTag(3, 2);
                byte[] typeTagBytes = varIntEncode(typeTag);
                byte[] typeBytes = type.getBytes(StandardCharsets.UTF_8);
                byte[] typeLengthVarIntEncodeBytes = varIntEncode(typeBytes.length);
                totalSize += typeTagBytes.length + typeLengthVarIntEncodeBytes.length + typeBytes.length;
    
                ByteBuffer byteBuffer = ByteBuffer.allocate(totalSize);
                byteBuffer
                        .put(serializeTypeTagBytes)
                        .put(serializeTypeLengthVarIntEncodeBytes)
                        .put(serializeTypeBytes);
                if (data != null) {
                    byteBuffer
                            .put(varIntEncode(dataTag))
                            .put(varIntEncode(data.length))
                            .put(data);
                }
                byteBuffer.put(typeTagBytes).put(typeLengthVarIntEncodeBytes).put(typeBytes);
                return byteBuffer.array();
            }
}
