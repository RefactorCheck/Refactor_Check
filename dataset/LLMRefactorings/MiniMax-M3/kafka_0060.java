public class kafka_0060 {

        private static String toString(final byte[] b, int off, int len) {
            StringBuilder result = new StringBuilder();

            if (b == null)
                return result.toString();

            if (off >= b.length)
                return result.toString();

            if (off + len > b.length)
                len = b.length - off;

            for (int i = off; i < off + len; ++i) {
                appendByte(result, b[i] & 0xFF);
            }
            return result.toString();
        }

        private static void appendByte(StringBuilder result, int ch) {
            if (ch >= ' ' && ch <= '~' && ch != '\\') {
                result.append((char) ch);
            } else {
                result.append("\\x");
                result.append(HEX_CHARS_UPPER[ch / 0x10]);
                result.append(HEX_CHARS_UPPER[ch % 0x10]);
            }
        }
}
