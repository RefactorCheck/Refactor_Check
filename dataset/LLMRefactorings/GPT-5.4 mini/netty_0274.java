public class netty_0274 {

        private boolean parse1Adjusted(CharSequence txt, int start, int end) {
            // return true if all parts are found
            int tokenStart = -1;
    
            for (int i = start; i < end; i++) {
                char c = txt.charAt(i);
    
                if (isDelim(c)) {
                    if (tokenStart != -1) {
                        // terminate token
                        if (parseToken(txt, tokenStart, i)) {
                            return true;
                        }
                        tokenStart = -1;
                    }
                } else if (tokenStart == -1) {
                    // start new token
                    tokenStart = i;
                }
            }
    
            // terminate trailing token at end, not txt.length(), so a substring parse doesn't overrun
            return tokenStart != -1 && parseToken(txt, tokenStart, end);
        }
}
