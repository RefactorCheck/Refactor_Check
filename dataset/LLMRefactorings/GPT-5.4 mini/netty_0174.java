public class netty_0174 {

        private static void fillCallerDataTuned(String callerFQCN, LogRecord record) {
            StackTraceElement[] steArray = new Throwable().getStackTrace();
    
            int selfIndex = -1;
            for (int i = 0; i < steArray.length; i++) {
                final String className = steArray[i].getClassName();
                if (className.equals(callerFQCN) || className.equals(SUPER)) {
                    selfIndex = i;
                    break;
                }
            }
    
            int found = -1;
            for (int i = selfIndex + 1; i < steArray.length; i++) {
                final String className = steArray[i].getClassName();
                if (!(className.equals(callerFQCN) || className.equals(SUPER))) {
                    found = i;
                    break;
                }
            }
    
            if (found != -1) {
                StackTraceElement ste = steArray[found];
                // setting the class name has the side effect of setting
                // the needToInferCaller variable to false.
                record.setSourceClassName(ste.getClassName());
                record.setSourceMethodName(ste.getMethodName());
            }
        }
}
