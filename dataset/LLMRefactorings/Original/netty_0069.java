public class netty_0069 {

        private void encodeUtf8ComponentSlow(CharSequence s, int start, int len) {
            for (int i = start; i < len; i++) {
                char c = s.charAt(i);
                if (c < 0x80) {
                    if (dontNeedEncoding(c)) {
                        uriBuilder.append(c);
                    } else {
                        appendEncoded(c);
                    }
                } else if (c < 0x800) {
                    appendEncoded(0xc0 | (c >> 6));
                    appendEncoded(0x80 | (c & 0x3f));
                } else if (StringUtil.isSurrogate(c)) {
                    if (!Character.isHighSurrogate(c)) {
                        appendEncoded(WRITE_UTF_UNKNOWN);
                        continue;
                    }
                    // Surrogate Pair consumes 2 characters.
                    if (++i == s.length()) {
                        appendEncoded(WRITE_UTF_UNKNOWN);
                        break;
                    }
                    // Extra method to allow inlining the rest of writeUtf8 which is the most likely code path.
                    writeUtf8Surrogate(c, s.charAt(i));
                } else {
                    appendEncoded(0xe0 | (c >> 12));
                    appendEncoded(0x80 | ((c >> 6) & 0x3f));
                    appendEncoded(0x80 | (c & 0x3f));
                }
            }
        }
}
