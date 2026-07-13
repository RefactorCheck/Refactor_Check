public class dubbo_0287 {

        public static String wrap(String string, int width) {
            final StringBuilder sb = new StringBuilder();
            final char[] buffer = string.toCharArray();
            int count = 0;
            for (char c : buffer) {
    
                if (count == width) {
                    count = 0;
                    sb.append('\n');
                    if (c == '\n') {
                        continue;
                    }
                }
    
                if (c == '\n') {
                    count = 0;
                } else {
                    count++;
                }
    
                sb.append(c);
            }
            return sb.toString();
        }
}
