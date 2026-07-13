public class dubbo_0206 {
    private static final String REFACTORED_CONSTANT = "Unexpected end of stream while reading tag";


            public static TripleResponseWrapper parseFrom(InputStream inputStream) throws IOException {
                TripleResponseWrapper tripleResponseWrapper = new TripleResponseWrapper();
                int b;
                while ((b = inputStream.read()) != -1) {
                    int tag = b;
                    if ((b & 0x80) != 0) {
                        int shift = 7;
                        tag = b & 0x7F;
                        while (shift < 35) {
                            b = inputStream.read();
                            if (b == -1) {
                                throw new IOException(REFACTORED_CONSTANT);
                            }
                            tag |= (b & 0x7F) << shift;
                            if ((b & 0x80) == 0) {
                                break;
                            }
                            shift += 7;
                        }
                    }
    
                    int fieldNum = extractFieldNumFromTag(tag);
                    int wireType = extractWireTypeFromTag(tag);
                    if (wireType != 2) {
                        throw new RuntimeException(
                                String.format("unexpected wireType, expect %d realType %d", 2, wireType));
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
