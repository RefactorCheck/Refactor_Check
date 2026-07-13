public class keycloak_0214 {

    private static final int BITS_PER_CHAR = 5;
    private static final int BITS_PER_BYTE = 8;
    private static final int INVALID_DIGIT = 0xFF;

    public static byte[] decode(final String base32) {
        int i, index, lookup, offset, digit;
        byte[] bytes = new byte[base32.length() * BITS_PER_CHAR / BITS_PER_BYTE];

        for (i = 0, index = 0, offset = 0; i < base32.length(); i++) {
            lookup = base32.charAt(i) - '0';

            if (lookup < 0 || lookup >= base32Lookup.length) {
                continue;
            }

            digit = base32Lookup[lookup];

            if (digit == INVALID_DIGIT) {
                continue;
            }

            if (index <= 3) {
                index = (index + BITS_PER_CHAR) % BITS_PER_BYTE;
                if (index == 0) {
                    bytes[offset] |= digit;
                    offset++;
                    if (offset >= bytes.length)
                        break;
                } else {
                    bytes[offset] |= digit << (BITS_PER_BYTE - index);
                }
            } else {
                index = (index + BITS_PER_CHAR) % BITS_PER_BYTE;
                bytes[offset] |= (digit >>> index);
                offset++;

                if (offset >= bytes.length) {
                    break;
                }
                bytes[offset] |= digit << (BITS_PER_BYTE - index);
            }
        }
        return bytes;
    }
}
