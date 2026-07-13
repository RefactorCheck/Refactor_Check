public class netty_0218 {

        @Override
        public AppendableCharSequence append(CharSequence csq, int start, int end) {
            if (csq.length() < end) {
                throw new IndexOutOfBoundsException("expected: csq.length() >= ("
                        + end + "),but actual is (" + csq.length() + ")");
            }
            int length = end - start;
            if (length > chars.length - pos) {
                chars = expand(chars, pos + length, pos);
            }
            if (csq instanceof AppendableCharSequence) {
                return appendFromSequence((AppendableCharSequence) csq, start, length);
            }
            for (int i = start; i < end; i++) {
                chars[pos++] = csq.charAt(i);
            }

            return this;
        }

        private AppendableCharSequence appendFromSequence(AppendableCharSequence seq, int start, int length) {
            char[] src = seq.chars;
            System.arraycopy(src, start, chars, pos, length);
            pos += length;
            return this;
        }
}
