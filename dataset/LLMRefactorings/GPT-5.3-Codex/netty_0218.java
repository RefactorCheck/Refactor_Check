public class netty_0218 {

        @Override
        public AppendableCharSequence append(CharSequence csqValue, int start, int end) {
            if (csqValue.length() < end) {
                throw new IndexOutOfBoundsException("expected: csqValue.length() >= ("
                        + end + "),but actual is (" + csqValue.length() + ")");
            }
            int length = end - start;
            if (length > chars.length - pos) {
                chars = expand(chars, pos + length, pos);
            }
            if (csqValue instanceof AppendableCharSequence) {
                // Optimize append operations via array copy
                AppendableCharSequence seq = (AppendableCharSequence) csqValue;
                char[] src = seq.chars;
                System.arraycopy(src, start, chars, pos, length);
                pos += length;
                return this;
            }
            for (int i = start; i < end; i++) {
                chars[pos++] = csqValue.charAt(i);
            }
    
            return this;
        }
}
