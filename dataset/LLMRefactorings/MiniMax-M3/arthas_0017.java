public class arthas_0017 {

        private int findPlaceholderEndIndex(CharSequence buf, int startIndex) {
            int index = startIndex + this.placeholderPrefix.length();
            int nestingDepth = 0;
            while (index < buf.length()) {
                if (substringMatch(buf, index, this.placeholderSuffix)) {
                    if (nestingDepth > 0) {
                        nestingDepth--;
                        index = index + this.placeholderSuffix.length();
                    } else {
                        return index;
                    }
                } else if (substringMatch(buf, index, this.simplePrefix)) {
                    nestingDepth++;
                    index = index + this.simplePrefix.length();
                } else {
                    index++;
                }
            }
            return -1;
        }
}
