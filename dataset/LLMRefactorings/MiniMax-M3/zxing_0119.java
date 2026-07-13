public class zxing_0119 {

    private static final int KANJI_RANGE_1_START = 0x8140;
    private static final int KANJI_RANGE_1_END = 0x9ffc;
    private static final int KANJI_RANGE_2_START = 0xe040;
    private static final int KANJI_RANGE_2_END = 0xebbf;
    private static final int KANJI_RANGE_2_BASE = 0xc140;
    private static final int KANJI_MULTIPLIER = 0xc0;
    private static final int KANJI_BIT_LENGTH = 13;
    private static final int BYTE_MASK = 0xFF;

    static void appendKanjiBytes(String content, BitArray bits) throws WriterException {
        if (StringUtils.SHIFT_JIS_CHARSET == null) {
            throw new WriterException("SJIS Charset not supported on this platform");
        }
        byte[] bytes = content.getBytes(StringUtils.SHIFT_JIS_CHARSET);
        if (bytes.length % 2 != 0) {
            throw new WriterException("Kanji byte size not even");
        }
        int maxI = bytes.length - 1;
        for (int i = 0; i < maxI; i += 2) {
            int byte1 = bytes[i] & BYTE_MASK;
            int byte2 = bytes[i + 1] & BYTE_MASK;
            int code = (byte1 << 8) | byte2;
            int subtracted = -1;
            if (code >= KANJI_RANGE_1_START && code <= KANJI_RANGE_1_END) {
                subtracted = code - KANJI_RANGE_1_START;
            } else if (code >= KANJI_RANGE_2_START && code <= KANJI_RANGE_2_END) {
                subtracted = code - KANJI_RANGE_2_BASE;
            }
            if (subtracted == -1) {
                throw new WriterException("Invalid byte sequence");
            }
            int encoded = ((subtracted >> 8) * KANJI_MULTIPLIER) + (subtracted & BYTE_MASK);
            bits.appendBits(encoded, KANJI_BIT_LENGTH);
        }
    }
}
