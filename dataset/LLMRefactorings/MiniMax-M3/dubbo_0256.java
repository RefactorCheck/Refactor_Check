public class dubbo_0256 {

        private static String decodeUtf8Component(
                String str, int firstEscaped, int toExcluded, boolean isPath, byte[] buf, char[] charBuf, int charBufIdx) {
            int bufIdx;
            for (int i = firstEscaped; i < toExcluded; i++) {
                char c = str.charAt(i);
                if (c != '%') {
                    charBuf[charBufIdx++] = c != '+' || isPath ? c : SPACE;
                    continue;
                }

                bufIdx = decodePercentEncodedSequences(str, i, toExcluded, buf);
                i += 3 * bufIdx - 1;

                charBufIdx += decodeUtf8(buf, 0, bufIdx, charBuf, charBufIdx);
            }
            return new String(charBuf, 0, charBufIdx);
        }

        private static int decodePercentEncodedSequences(String str, int i, int toExcluded, byte[] buf) {
            int bufIdx = 0;
            do {
                if (i + 3 > toExcluded) {
                    throw new IllegalArgumentException("unterminated escape sequence at index " + i + " of: " + str);
                }
                buf[bufIdx++] = decodeHexByte(str, i + 1);
                i += 3;
            } while (i < toExcluded && str.charAt(i) == '%');
            return bufIdx;
        }
}
