public class springframework_0173 {

    protected void initCounters() {
        int pos = 0;
        if (this.pattern != null) {
            while (pos < this.pattern.length()) {
                if (this.pattern.charAt(pos) == '{') {
                    this.uriVars++;
                    pos++;
                }
                else if (this.pattern.charAt(pos) == '*') {
                    pos = handleWildcard(pos);
                }
                else {
                    pos++;
                }
            }
        }
    }

    private int handleWildcard(int pos) {
        if (pos + 1 < this.pattern.length() && this.pattern.charAt(pos + 1) == '*') {
            this.doubleWildcards++;
            return pos + 2;
        }
        else if (pos > 0 && !this.pattern.substring(pos - 1).equals(".*")) {
            this.singleWildcards++;
            return pos + 1;
        }
        return pos + 1;
    }
}
