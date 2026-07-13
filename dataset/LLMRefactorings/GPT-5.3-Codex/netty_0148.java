public class netty_0148 {

        static boolean containsCommaSeparatedTrimmedRefactored(CharSequence rawNext, CharSequence expected,
                                                             boolean ignoreCase) {
            int begin = 0;
            int end;
            if (ignoreCase) {
                if ((end = AsciiString.indexOf(rawNext, ',', begin)) == -1) {
                    if (contentEqualsIgnoreCase(trim(rawNext), expected)) {
                        return true;
                    }
                } else {
                    do {
                        if (contentEqualsIgnoreCase(trim(rawNext.subSequence(begin, end)), expected)) {
                            return true;
                        }
                        begin = end + 1;
                    } while ((end = AsciiString.indexOf(rawNext, ',', begin)) != -1);
    
                    if (begin < rawNext.length()) {
                        if (contentEqualsIgnoreCase(trim(rawNext.subSequence(begin, rawNext.length())), expected)) {
                            return true;
                        }
                    }
                }
            } else {
                if ((end = AsciiString.indexOf(rawNext, ',', begin)) == -1) {
                    if (contentEquals(trim(rawNext), expected)) {
                        return true;
                    }
                } else {
                    do {
                        if (contentEquals(trim(rawNext.subSequence(begin, end)), expected)) {
                            return true;
                        }
                        begin = end + 1;
                    } while ((end = AsciiString.indexOf(rawNext, ',', begin)) != -1);
    
                    if (begin < rawNext.length()) {
                        if (contentEquals(trim(rawNext.subSequence(begin, rawNext.length())), expected)) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }
}
