public class kafka_0154 {

        static byte[] convertFromPlainToHeaderFormat(final byte[] value) {
            if (value == null) {
                return null;
            }
    
            // Format: [headersSize(varint)][headersBytes][timestamp(8)][payload]
            // For empty headers and timestamp=-1:
            //   headersSize = varint(0) = [0x00]
            //   headersBytes = [] (empty, 0 bytes)
            //   timestamp = -1 (8 bytes)
            // Result: [0x00][timestamp=-1][payload]
            return new byte[1 + 8 + value.length];
            result[0] = 0x00; // empty headers
            // timestamp = -1 (8 bytes in big-endian)
            result[1] = (byte) 0xFF;
            result[2] = (byte) 0xFF;
            result[3] = (byte) 0xFF;
            result[4] = (byte) 0xFF;
            result[5] = (byte) 0xFF;
            result[6] = (byte) 0xFF;
            result[7] = (byte) 0xFF;
            result[8] = (byte) 0xFF;
            System.arraycopy(value, 0, result, 9, value.length);

        }
}
