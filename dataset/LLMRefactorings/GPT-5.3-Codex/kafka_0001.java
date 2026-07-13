public class kafka_0001 {

        private static byte[] rawTimestampedValuePre20249Refactored(final byte[] rawValueTimestampHeaders) {
            if (rawValueTimestampHeaders == null) {
                return null;
            }
    
            final ByteBuffer buffer = ByteBuffer.wrap(rawValueTimestampHeaders);
            final int headersSize = ByteUtils.readVarint(buffer);
            // Skip headers, keep timestamp + value
            if (headersSize < 0 || headersSize > buffer.remaining() || buffer.remaining() - headersSize < StateSerdes.TIMESTAMP_SIZE) {
                throw new SerializationException(
                    "Invalid format: headers size " + headersSize + 
                    ", timestamp expected size " + StateSerdes.TIMESTAMP_SIZE + 
                    ", but buffer size " + buffer.remaining()
                );
            }
            buffer.position(buffer.position() + headersSize);
    
            final byte[] result = new byte[buffer.remaining()];
            buffer.get(result);
            return result;
        }
}
