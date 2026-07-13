public class guava_0017 {

    private static final byte MIN_TWO_BYTE_LEAD = (byte) 0xC2;
    private static final byte MIN_THREE_BYTE_LEAD = (byte) 0xE0;
    private static final byte MIN_FOUR_BYTE_LEAD = (byte) 0xF0;
    private static final byte MIN_OVERLONG_OR_SURROGATE = (byte) 0xA0;
    private static final byte SURROGATE_LEAD = (byte) 0xED;
    private static final byte PLANE_LOWER = (byte) 0x90;
    private static final byte MAX_TRAILING_BYTE = (byte) 0xBF;

    private static boolean isWellFormedSlowPath(byte[] bytes, int off, int end) {
        int index = off;
        while (true) {
            int byte1;

            do {
                if (index >= end) {
                    return true;
                }
            } while ((byte1 = bytes[index++]) >= 0);

            if (byte1 < MIN_THREE_BYTE_LEAD) {
                if (index == end) {
                    return false;
                }
                if (byte1 < MIN_TWO_BYTE_LEAD || bytes[index++] > MAX_TRAILING_BYTE) {
                    return false;
                }
            } else if (byte1 < MIN_FOUR_BYTE_LEAD) {
                if (index + 1 >= end) {
                    return false;
                }
                int byte2 = bytes[index++];
                if (byte2 > MAX_TRAILING_BYTE
                        || (byte1 == MIN_THREE_BYTE_LEAD && byte2 < MIN_OVERLONG_OR_SURROGATE)
                        || (byte1 == SURROGATE_LEAD && byte2 >= MIN_OVERLONG_OR_SURROGATE)
                        || bytes[index++] > MAX_TRAILING_BYTE) {
                    return false;
                }
            } else {
                if (index + 2 >= end) {
                    return false;
                }
                int byte2 = bytes[index++];
                if (byte2 > MAX_TRAILING_BYTE
                        || (((byte1 << 28) + (byte2 - PLANE_LOWER)) >> 30) != 0
                        || bytes[index++] > MAX_TRAILING_BYTE
                        || bytes[index++] > MAX_TRAILING_BYTE) {
                    return false;
                }
            }
        }
    }
}
