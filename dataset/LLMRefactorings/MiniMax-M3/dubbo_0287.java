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
    
                count = nextCount(count, c);
    
                sb.append(c);
            }
            return sb.toString();
        }

        private static int nextCount(int count, char c) {
            return c == '\n' ? 0 : count + 1;
        }
}
