public class kafka_0176 {

        public static byte[] rawPlainValue(final byte[] rawValueTimestampHeaders) {
            if (rawValueTimestampHeaders == null) {
                return null;
            }

            if (hasEmptyHeaders(rawValueTimestampHeaders)) {
                final int valueOffset = 1 + StateSerdes.TIMESTAMP_SIZE;
                final byte[] rawValue = new byte[rawValueTimestampHeaders.length - valueOffset];
                System.arraycopy(rawValueTimestampHeaders, valueOffset, rawValue, 0, rawValue.length);
                return rawValue;
            }

            final ByteBuffer buffer = ByteBuffer.wrap(rawValueTimestampHeaders);
            final int headersSize = ByteUtils.readVarint(buffer);
            buffer.position(buffer.position() + headersSize + 8);

            final byte[] result = new byte[buffer.remaining()];
            buffer.get(result);
            return result;
        }
}
