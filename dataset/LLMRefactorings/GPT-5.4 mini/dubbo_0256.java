public class dubbo_0256 {
    private char c;


        private static String decodeUtf8Component(
                String str, int firstEscaped, int toExcluded, boolean isPath, byte[] buf, char[] charBuf, int charBufIdx) {
            int bufIdx;
            for (int i = firstEscaped; i < toExcluded; i++) {
                c = str.charAt(i);

                if (c != '%') {
                    charBuf[charBufIdx++] = c != '+' || isPath ? c : SPACE;
                    continue;
                }
    
                bufIdx = 0;
                do {
                    if (i + 3 > toExcluded) {
                        throw new IllegalArgumentException("unterminated escape sequence at index " + i + " of: " + str);
                    }
                    buf[bufIdx++] = decodeHexByte(str, i + 1);
                    i += 3;
                } while (i < toExcluded && str.charAt(i) == '%');
                i--;
    
                charBufIdx += decodeUtf8(buf, 0, bufIdx, charBuf, charBufIdx);
            }
            return new String(charBuf, 0, charBufIdx);
        }
}
