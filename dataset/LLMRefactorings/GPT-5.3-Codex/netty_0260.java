public class netty_0260 {

        private static int utf8BytesNonAscii(final CharSequence seq, final int start, final int end) {
            int encodedLength = 0;
            for (int i = start; i < end; i++) {

                // making it 100% branchless isn't rewarding due to the many bit operations necessary!
                if ((seq.charAt(i)) < 0x800) {
                    // branchless version of: ((seq.charAt(i)) <= 127 ? 0:1) + 1
                    encodedLength += ((0x7f - (seq.charAt(i))) >>> 31) + 1;
                } else if (isSurrogate((seq.charAt(i)))) {
                    if (!Character.isHighSurrogate((seq.charAt(i)))) {
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
