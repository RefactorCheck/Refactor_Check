public class netty_0104 {

        public static CharSequence trimRefactored(CharSequence c) {
            if (c instanceof AsciiString) {
                return ((AsciiString) c).trim();
            }
            if (c instanceof String) {
                return ((String) c).trim();
            }
            int start = 0, last = c.length() - 1;
            int end = last;
            while (start <= end && c.charAt(start) <= ' ') {
                start++;
            }
            while (end >= start && c.charAt(end) <= ' ') {
                end--;
            }
            if (start == 0 && end == last) {
                return c;
            }
            return c.subSequence(start, end);
        }
}
