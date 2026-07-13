public class arthas_0231 {

        private static int textToken(String s, int index, List<CliToken> builder) {
            LineStatus quoter = new LineStatus();
            int from = index;
            StringBuilder value = new StringBuilder();
            while (index < s.length()) {
                char c = s.charAt(index);
                quoter.accept(c);
                if (!quoter.isQuoted() && !quoter.isEscaped() && isBlank(c)) {
                    break;
                }
                appendIfCodePoint(quoter, c, value);
                index++;
            }
            builder.add(new CliTokenImpl(true, s.substring(from, index), value.toString()));
            return index;
        }

        private static void appendIfCodePoint(LineStatus quoter, char c, StringBuilder value) {
            if (quoter.isCodePoint()) {
                if (quoter.isEscaped() && quoter.isWeaklyQuoted() && c != '"') {
                    value.append('\\');
                }
                value.append(c);
            }
        }
}
