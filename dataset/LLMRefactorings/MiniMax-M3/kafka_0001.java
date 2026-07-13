public class kafka_0001 {

        private static byte[] rawTimestampedValuePre20249(final byte[] rawValueTimestampHeaders) {
            if (rawValueTimestampHeaders == null) {
                return null;
            }
    
            final ByteBuffer buffer = ByteBuffer.wrap(rawValueTimestampHeaders);
            final int headersSize = ByteUtils.readVarint(buffer);
            validateHeadersSize(headersSize, buffer.remaining());
            buffer.position(buffer.position() + headersSize);
    
            final byte[] result = new byte[buffer.remaining()];
            buffer.get(result);
            return result;
        }
    
        private static void validateHeadersSize(final int headersSize, final int bufferRemaining) {
            if (headersSize < 0 || headersSize > bufferRemaining || bufferRemaining - headersSize < StateSerdes.TIMESTAMP_SIZE) {
                throw new SerializationException(
                    "Invalid format: headers size " + headersSize + 
                    ", timestamp expected size " + StateSerdes.TIMESTAMP_SIZE + 
                    ", but buffer size " + bufferRemaining
                );
            }
        }
}
