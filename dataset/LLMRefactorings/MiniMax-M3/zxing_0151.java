public class zxing_0151 {

    private static final int ALPHANUMERIC_PAIR_MULTIPLIER = 45;
    private static final int BITS_PER_ALPHANUMERIC_PAIR = 11;
    private static final int BITS_PER_ALPHANUMERIC_SINGLE = 6;
    private static final int INVALID_CODE = -1;

    static void appendAlphanumericBytes(CharSequence content, BitArray bits) throws WriterException {
        int length = content.length();
        int i = 0;
        while (i < length) {
            int code1 = getAlphanumericCode(content.charAt(i));
            if (code1 == INVALID_CODE) {
                throw new WriterException();
            }
            if (i + 1 < length) {
                int code2 = getAlphanumericCode(content.charAt(i + 1));
                if (code2 == INVALID_CODE) {
                    throw new WriterException();
                }
                bits.appendBits(code1 * ALPHANUMERIC_PAIR_MULTIPLIER + code2, BITS_PER_ALPHANUMERIC_PAIR);
                i += 2;
            } else {
                bits.appendBits(code1, BITS_PER_ALPHANUMERIC_SINGLE);
                i++;
            }
        }
    }
}
