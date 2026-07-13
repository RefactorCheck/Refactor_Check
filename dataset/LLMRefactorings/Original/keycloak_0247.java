public class keycloak_0247 {

        private static Object stackTraceObject(final String prefix) {
            return new Object() {
                @Override
                public String toString() {
                    StringBuilder sb = new StringBuilder();
                    StackTraceElement[] stackTrace = (new Throwable()).getStackTrace();
                    boolean stackTraceStarted = false;
                    for (int endIndex = 0; endIndex < stackTrace.length; endIndex++) {
                        StackTraceElement st = stackTrace[endIndex];
                        if (! stackTraceStarted) {
                            stackTraceStarted = (getClass().getName().equals(st.getClassName()));
                            endIndex++;
                            continue;
                        }
                        if (IGNORED.matcher(st.getClassName()).find()) {
                            continue;
                        }
                        if (st.getClassName().startsWith("org.jboss.resteasy")) {
                            break;
                        }
                        sb.append(prefix).append(st);
                    }
                    return sb.toString();
                }
            };
        }
}
