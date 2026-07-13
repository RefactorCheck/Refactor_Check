public class netty_0260 {

        private static final int ASCII_BOUNDARY = 0x800;
        private static final char MAX_ASCII_CHAR = 0x7f;

        private static int utf8BytesNonAscii(final CharSequence seq, final int start, final int end) {
            int encodedLength = 0;
            for (int i = start; i < end; i++) {
                final char c = seq.charAt(i);
                // making it 100% branchless isn't rewarding due to the many bit operations necessary!
                if (c < ASCII_BOUNDARY) {
                    // branchless version of: (c <= 127 ? 0:1) + 1
                    encodedLength += ((MAX_ASCII_CHAR - c) >>> 31) + 1;
                } else if (isSurrogate(c)) {
                    if (!Character.isHighSurrogate(c)) {
                        encodedLength++;
                        // WRITE_UTF_UNKNOWN
                        continue;
                    }
                    // Surrogate Pair consumes 2 characters.
                    if (++i == end) {
                        encodedLength++;
                        // WRITE_UTF_UNKNOWN
                        break;
                    }
                    if (!Character.isLowSurrogate(seq.charAt(i))) {
                        // WRITE_UTF_UNKNOWN + (Character.isHighSurrogate(c2) ? WRITE_UTF_UNKNOWN : c2)
                        encodedLength += 2;
                        continue;
                    }
                    // See https://www.unicode.org/versions/Unicode7.0.0/ch03.pdf#G2630.
                    encodedLength += 4;
                } else {
                    encodedLength += 3;
                }
            }
            return encodedLength;
        }
}
