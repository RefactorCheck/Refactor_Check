public class kafka_0285 {

    private static int sizeOf(int keySize, int valueSize, Header[] headers) {
        int size = 0;
        size += calculateFieldSize(keySize);
        size += calculateFieldSize(valueSize);

        if (headers == null)
            throw new IllegalArgumentException("Headers cannot be null");

        size += ByteUtils.sizeOfVarint(headers.length);
        for (Header header : headers) {
            size += sizeOfHeader(header);
        }
        return size;
    }

    private static int calculateFieldSize(int fieldSize) {
        if (fieldSize < 0)
            return NULL_VARINT_SIZE_BYTES;
        else
            return ByteUtils.sizeOfVarint(fieldSize) + fieldSize;
    }

    private static int sizeOfHeader(Header header) {
        String headerKey = header.key();
        if (headerKey == null)
            throw new IllegalArgumentException("Invalid null header key found in headers");

        int headerKeySize = Utils.utf8Length(headerKey);
        int size = ByteUtils.sizeOfVarint(headerKeySize) + headerKeySize;

        byte[] headerValue = header.value();
        if (headerValue == null) {
            size += NULL_VARINT_SIZE_BYTES;
        } else {
            size += ByteUtils.sizeOfVarint(headerValue.length) + headerValue.length;
        }
        return size;
    }
}
