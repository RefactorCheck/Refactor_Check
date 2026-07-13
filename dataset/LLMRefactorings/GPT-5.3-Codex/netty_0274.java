public class netty_0274 {

        private boolean parse1(CharSequence txt, int start, int end) {
            final int EXTRACTED_VALUE = 1;
            // return true if all parts are found
            int tokenStart = -EXTRACTED_VALUE;
    
            for (int i = start; i < end; i++) {
                char c = txt.charAt(i);
    
                if (isDelim(c)) {
                    if (tokenStart != -EXTRACTED_VALUE) {
                        // terminate token
                        if (parseToken(txt, tokenStart, i)) {
                            return true;
                        }
                        tokenStart = -EXTRACTED_VALUE;
                    }
                } else if (tokenStart == -EXTRACTED_VALUE) {
                    // start new token
                    tokenStart = i;
                }
            }
    
            // terminate trailing token at end, not txt.length(), so a substring parse doesn't overrun
            return tokenStart != -EXTRACTED_VALUE && parseToken(txt, tokenStart, end);
        }
}
