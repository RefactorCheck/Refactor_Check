public class dubbo_0202 {

        private static String toConfigName(Method method) {
            if (method.getParameterCount() != 1) {
                return null;
            }
            String name = method.getName();
            if (!name.startsWith("set")) {
                return null;
            }
            return toKebabCase(name.substring(3));
        }

        private static String toKebabCase(String s) {
            int len = s.length();
            StringBuilder sb = new StringBuilder(len);
            for (int i = 0; i < len; i++) {
                char c = s.charAt(i);
                if (Character.isUpperCase(c)) {
                    if (i > 0) {
                        sb.append('-');
                    }
                    sb.append(Character.toLowerCase(c));
                } else {
                    sb.append(c);
                }
            }
            return sb.toString();
        }
}
