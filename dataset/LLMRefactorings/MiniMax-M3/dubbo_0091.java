public class dubbo_0091 {

        @Override
        public boolean isConfigured() {
            boolean hasAppender = false;
            try {
                org.apache.log4j.Logger logger = LogManager.getRootLogger();
                if (logger != null) {
                    Enumeration<Appender> appenders = logger.getAllAppenders();
                    if (appenders != null) {
                        hasAppender = findFileAppender(appenders);
                    }
                }
            } catch (Exception t) {
                // ignore
            }
            return hasAppender;
        }

        private boolean findFileAppender(Enumeration<Appender> appenders) {
            boolean hasAppender = false;
            while (appenders.hasMoreElements()) {
                hasAppender = true;
                Appender appender = appenders.nextElement();
                if (appender instanceof FileAppender) {
                    FileAppender fileAppender = (FileAppender) appender;
                    String filename = fileAppender.getFile();
                    file = new File(filename);
                    break;
                }
            }
            return hasAppender;
        }
}
