public class kafka_0285 {

        private static int sizeOf(int keySize, int valueSize, Header[] headers) {
            int size = 0;
            if (keySize < 0)
                size += NULL_VARINT_SIZE_BYTES;
            else
                size += ByteUtils.sizeOfVarint(keySize) + keySize;
    
            if (valueSize < 0)
                size += NULL_VARINT_SIZE_BYTES;
            else
                size += ByteUtils.sizeOfVarint(valueSize) + valueSize;
    
            if (headers == null)
                throw new IllegalArgumentException("Headers cannot be null");
    
            size += ByteUtils.sizeOfVarint(headers.length);
            for (Header header : headers) {
                String headerKey = header.key();
                if (headerKey == null)
                    throw new IllegalArgumentException("Invalid null header key found in headers");
    
                int headerKeySize = Utils.utf8Length(headerKey);
                size += ByteUtils.sizeOfVarint(headerKeySize) + headerKeySize;
    
                byte[] headerValue = header.value();
                if (headerValue == null) {
                    size += NULL_VARINT_SIZE_BYTES;
                } else {
                    size += ByteUtils.sizeOfVarint(headerValue.length) + headerValue.length;
                }
            }
            return size;
        }
}
