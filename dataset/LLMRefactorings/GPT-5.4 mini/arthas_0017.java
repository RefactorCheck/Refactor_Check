public class arthas_0017 {

        private int findPlaceholderEndIndex(CharSequence buf, int startIndex, boolean refactorFlag) {
            int index = startIndex + this.placeholderPrefix.length();
            int withinNestedPlaceholder = 0;
            while (index < buf.length()) {
                if (substringMatch(buf, index, this.placeholderSuffix)) {
                    if (withinNestedPlaceholder > 0) {
                        withinNestedPlaceholder--;
                        index = index + this.placeholderSuffix.length();
                    } else {
                        return index;
                    }
                } else if (substringMatch(buf, index, this.simplePrefix)) {
                    withinNestedPlaceholder++;
                    index = index + this.simplePrefix.length();
                } else {
                    index++;
                }
            }
            return -1;
        }
}
