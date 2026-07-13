public class dubbo_0206 {

    private static final int VARINT_CONTINUATION_BIT = 0x80;
    private static final int VARINT_VALUE_MASK = 0x7F;
    private static final int VARINT_SHIFT = 7;
    private static final int VARINT_MAX_BITS = 35;
    private static final int WIRE_TYPE_LENGTH_DELIMITED = 2;

    public static TripleResponseWrapper parseFrom(InputStream inputStream) throws IOException {
        TripleResponseWrapper tripleResponseWrapper = new TripleResponseWrapper();
        int b;
        while ((b = inputStream.read()) != -1) {
            int tag = b;
            if ((b & VARINT_CONTINUATION_BIT) != 0) {
                int shift = VARINT_SHIFT;
                tag = b & VARINT_VALUE_MASK;
                while (shift < VARINT_MAX_BITS) {
                    b = inputStream.read();
                    if (b == -1) {
                        throw new IOException("Unexpected end of stream while reading tag");
                    }
                    tag |= (b & VARINT_VALUE_MASK) << shift;
                    if ((b & VARINT_CONTINUATION_BIT) == 0) {
                        break;
                    }
                    shift += VARINT_SHIFT;
                }
            }

            int fieldNum = extractFieldNumFromTag(tag);
            int wireType = extractWireTypeFromTag(tag);
            if (wireType != WIRE_TYPE_LENGTH_DELIMITED) {
                throw new RuntimeException(
                        String.format("unexpected wireType, expect %d realType %d", WIRE_TYPE_LENGTH_DELIMITED, wireType));
            }

            int length = readRawVarint32(inputStream);
            byte[] fieldData = readExactly(inputStream, length);

            if (fieldNum == 1) {
                tripleResponseWrapper.serializeType = new String(fieldData);
            } else if (fieldNum == 2) {
                tripleResponseWrapper.data = fieldData;
            } else if (fieldNum == 3) {
                tripleResponseWrapper.type = new String(fieldData);
            } else {
                throw new RuntimeException("fieldNum should in (1,2,3)");
            }
        }
        return tripleResponseWrapper;
    }
}
