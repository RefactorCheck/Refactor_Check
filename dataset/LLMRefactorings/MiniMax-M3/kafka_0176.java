public class kafka_0176 {

    public static byte[] rawPlainValue(final byte[] rawValueTimestampHeaders) {
        if (rawValueTimestampHeaders == null) {
            return null;
        }

        if (hasEmptyHeaders(rawValueTimestampHeaders)) {
            return extractValueWithoutHeaders(rawValueTimestampHeaders);
        }

        return extractValueWithHeaders(rawValueTimestampHeaders);
    }

    private static byte[] extractValueWithoutHeaders(final byte[] rawValueTimestampHeaders) {
        final byte[] rawValue = new byte[rawValueTimestampHeaders.length - 1 - StateSerdes.TIMESTAMP_SIZE];
        System.arraycopy(rawValueTimestampHeaders, 1 + StateSerdes.TIMESTAMP_SIZE, rawValue, 0, rawValue.length);
        return rawValue;
    }

    private static byte[] extractValueWithHeaders(final byte[] rawValueTimestampHeaders) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawValueTimestampHeaders);
        final int headersSize = ByteUtils.readVarint(buffer);
        buffer.position(buffer.position() + headersSize + 8);

        final byte[] result = new byte[buffer.remaining()];
        buffer.get(result);
        return result;
    }
}
