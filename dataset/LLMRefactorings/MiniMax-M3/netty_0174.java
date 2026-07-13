public class netty_0174 {

        private static void fillCallerData(String callerFQCN, LogRecord record) {
            StackTraceElement[] steArray = new Throwable().getStackTrace();
            int selfIndex = findSelfIndex(steArray, callerFQCN);
            int found = findCalleeIndex(steArray, selfIndex, callerFQCN);
            if (found != -1) {
                StackTraceElement ste = steArray[found];
                record.setSourceClassName(ste.getClassName());
                record.setSourceMethodName(ste.getMethodName());
            }
        }

        private static int findSelfIndex(StackTraceElement[] steArray, String callerFQCN) {
            for (int i = 0; i < steArray.length; i++) {
                final String className = steArray[i].getClassName();
                if (className.equals(callerFQCN) || className.equals(SUPER)) {
                    return i;
                }
            }
            return -1;
        }

        private static int findCalleeIndex(StackTraceElement[] steArray, int selfIndex, String callerFQCN) {
            for (int i = selfIndex + 1; i < steArray.length; i++) {
                final String className = steArray[i].getClassName();
                if (!(className.equals(callerFQCN) || className.equals(SUPER))) {
                    return i;
                }
            }
            return -1;
        }
}
