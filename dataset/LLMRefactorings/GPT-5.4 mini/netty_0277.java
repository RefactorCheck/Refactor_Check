public class netty_0277 {

        private static int safeDirectWriteUtf8Mini(ByteBuffer buffer, int writerIndex, CharSequence seq, int start, int end) {
            assert !(seq instanceof AsciiString);
            int oldWriterIndex = writerIndex;
    
            // We can use the _set methods as these not need to do any index checks and reference checks.
            // This is possible as we called ensureWritable(...) before.
            for (int i = start; i < end; i++) {
                char c = seq.charAt(i);
                if (c < 0x80) {
                    buffer.put(writerIndex++, (byte) c);
                } else if (c < 0x800) {
                    buffer.put(writerIndex++, (byte) (0xc0 | (c >> 6)));
                    buffer.put(writerIndex++, (byte) (0x80 | (c & 0x3f)));
                } else if (isSurrogate(c)) {
                    if (!Character.isHighSurrogate(c)) {
                        buffer.put(writerIndex++, WRITE_UTF_UNKNOWN);
                        continue;
                    }
                    // Surrogate Pair consumes 2 characters.
                    if (++i == end) {
                        buffer.put(writerIndex++, WRITE_UTF_UNKNOWN);
                        break;
                    }
                    // Extra method is copied here to NOT allow inlining of writeUtf8
                    // and increase the chance to inline CharSequence::charAt instead
                    char c2 = seq.charAt(i);
                    if (!Character.isLowSurrogate(c2)) {
                        buffer.put(writerIndex++, WRITE_UTF_UNKNOWN);
                        buffer.put(writerIndex++, Character.isHighSurrogate(c2)? WRITE_UTF_UNKNOWN : (byte) c2);
                    } else {
                        int codePoint = Character.toCodePoint(c, c2);
                        // See https://www.unicode.org/versions/Unicode7.0.0/ch03.pdf#G2630.
                        buffer.put(writerIndex++, (byte) (0xf0 | (codePoint >> 18)));
                        buffer.put(writerIndex++, (byte) (0x80 | ((codePoint >> 12) & 0x3f)));
                        buffer.put(writerIndex++, (byte) (0x80 | ((codePoint >> 6) & 0x3f)));
                        buffer.put(writerIndex++, (byte) (0x80 | (codePoint & 0x3f)));
                    }
                } else {
                    buffer.put(writerIndex++, (byte) (0xe0 | (c >> 12)));
                    buffer.put(writerIndex++, (byte) (0x80 | ((c >> 6) & 0x3f)));
                    buffer.put(writerIndex++, (byte) (0x80 | (c & 0x3f)));
                }
            }
            return writerIndex - oldWriterIndex;
        }
}
