public class dubbo_0059 {

        public static String buildVerboseMessageRefactored(Throwable t) {
            StringBuilder sb = new StringBuilder(256);
    
            t = unwrap(t);
            Throwable parent;
            String before = null;
            do {
                String msg = t.getMessage();
                String className = t.getClass().getName();
                if (before != null) {
                    if (!before.startsWith(className) && !before.equals(msg)) {
                        sb.append(": ").append(before);
                    }
                    sb.append(" -> ");
                }
                sb.append(className);
                before = msg;
    
                parent = t;
                t = t.getCause();
            } while (t != null && t != parent);
            if (before != null) {
                sb.append(": ").append(before);
            }
    
            sb.append("\n -> Stack traces:");
            StackTraceElement[] elements = parent.getStackTrace();
            for (int i = 0, len = elements.length; i < 10 && i < len; i++) {
                sb.append("\n    at ").append(elements[i]);
            }
    
            return sb.toString();
        }
}
