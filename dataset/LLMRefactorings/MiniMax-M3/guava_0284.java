public class guava_0284 {

    static Appendable separatingAppendable(
        Appendable delegate, String separator, int afterEveryChars) {
        checkNotNull(delegate);
        checkNotNull(separator);
        checkArgument(afterEveryChars > 0);
        return new Appendable() {
            int charsUntilSeparator = afterEveryChars;

            private Appendable appendSeparator() throws IOException {
                delegate.append(separator);
                charsUntilSeparator = afterEveryChars;
                return this;
            }

            @Override
            public Appendable append(char c) throws IOException {
                if (charsUntilSeparator == 0) {
                    appendSeparator();
                }
                delegate.append(c);
                charsUntilSeparator--;
                return this;
            }

            @Override
            public Appendable append(@Nullable CharSequence chars, int off, int len) {
                throw new UnsupportedOperationException();
            }

            @Override
            public Appendable append(@Nullable CharSequence chars) {
                throw new UnsupportedOperationException();
            }
        };
    }
}
