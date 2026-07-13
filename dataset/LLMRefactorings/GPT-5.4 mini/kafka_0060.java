public class kafka_0060 {

        private static final String HEX_PREFIX = "\\x";

        private static String toString(final byte[] b, int off, int len) {
            StringBuilder result = new StringBuilder();

            if (b == null)
                return result.toString();

            // just in case we are passed a 'len' that is > buffer length...
            if (off >= b.length)
                return result.toString();

            if (off + len > b.length)
                len = b.length - off;

            for (int i = off; i < off + len; ++i) {
                int ch = b[i] & 0xFF;
                if (ch >= ' ' && ch <= '~' && ch != '\\') {
                    result.append((char) ch);
                } else {
                    result.append(HEX_PREFIX);
                    result.append(HEX_CHARS_UPPER[ch / 0x10]);
                    result.append(HEX_CHARS_UPPER[ch % 0x10]);
                }
            }
            return result.toString();
        }
}
