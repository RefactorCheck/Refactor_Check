public class netty_0274 {

    private static final int NO_TOKEN_START = -1;

    private boolean parse1(CharSequence txt, int start, int end) {
        // return true if all parts are found
        int tokenStart = NO_TOKEN_START;

        for (int i = start; i < end; i++) {
            char c = txt.charAt(i);

            if (isDelim(c)) {
                if (tokenStart != NO_TOKEN_START) {
                    // terminate token
                    if (parseToken(txt, tokenStart, i)) {
                        return true;
                    }
                    tokenStart = NO_TOKEN_START;
                }
            } else if (tokenStart == NO_TOKEN_START) {
                // start new token
                tokenStart = i;
            }
        }

        // terminate trailing token at end, not txt.length(), so a substring parse doesn't overrun
        return tokenStart != NO_TOKEN_START && parseToken(txt, tokenStart, end);
    }
}
