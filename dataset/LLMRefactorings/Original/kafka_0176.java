public class kafka_0176 {

        public static byte[] rawPlainValue(final byte[] rawValueTimestampHeaders) {
            if (rawValueTimestampHeaders == null) {
                return null;
            }
    
            // If the header is empty, then copy the value bytes directly
            if (hasEmptyHeaders(rawValueTimestampHeaders)) {
                // Strip header size (varint 1 byte), empty headers (no bytes), and timestamp
                final byte[] rawValue = new byte[rawValueTimestampHeaders.length - 1 - StateSerdes.TIMESTAMP_SIZE]; 
                System.arraycopy(rawValueTimestampHeaders, 1 + StateSerdes.TIMESTAMP_SIZE, rawValue, 0, rawValue.length);
                return rawValue;
            }
    
            final ByteBuffer buffer = ByteBuffer.wrap(rawValueTimestampHeaders);
            final int headersSize = ByteUtils.readVarint(buffer);
            // Skip headers and timestamp (8 bytes)
            buffer.position(buffer.position() + headersSize + 8);
    
            final byte[] result = new byte[buffer.remaining()];
            buffer.get(result);
            return result;
        }
}
