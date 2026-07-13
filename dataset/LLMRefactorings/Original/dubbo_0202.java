public class dubbo_0202 {

        private static String toConfigName(Method method) {
            if (method.getParameterCount() != 1) {
                return null;
            }
            String name = method.getName();
            if (!name.startsWith("set")) {
                return null;
            }
            int len = name.length();
            StringBuilder sb = new StringBuilder(len);
            for (int i = 3; i < len; i++) {
                char c = name.charAt(i);
                if (Character.isUpperCase(c)) {
                    if (i > 3) {
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
