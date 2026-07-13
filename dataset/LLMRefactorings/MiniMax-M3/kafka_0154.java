public class kafka_0154 {

        static byte[] convertFromPlainToHeaderFormat(final byte[] value) {
            if (value == null) {
                return null;
            }

            final byte[] result = new byte[1 + 8 + value.length];
            result[0] = 0x00;
            writeNegativeOneTimestamp(result, 1);
            System.arraycopy(value, 0, result, 9, value.length);
            return result;
        }

        private static void writeNegativeOneTimestamp(byte[] result, int offset) {
            // timestamp = -1 (8 bytes in big-endian)
            result[offset] = (byte) 0xFF;
            result[offset + 1] = (byte) 0xFF;
            result[offset + 2] = (byte) 0xFF;
            result[offset + 3] = (byte) 0xFF;
            result[offset + 4] = (byte) 0xFF;
            result[offset + 5] = (byte) 0xFF;
            result[offset + 6] = (byte) 0xFF;
            result[offset + 7] = (byte) 0xFF;
        }
}
