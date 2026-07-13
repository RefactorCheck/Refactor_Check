public class arthas_0107 {
    private static final String REFACTORED_CONSTANT = "org.apache.log4j.Logger";


        private void updateLoggerType(LoggerTypes loggerTypes, ClassLoader classLoader, String className) {
            if (REFACTORED_CONSTANT.equals(className)) {
                // 判断 org.apache.log4j.AsyncAppender 是否存在，如果存在则是 log4j，不是slf4j-over-log4j
                try {
                    if (classLoader.getResource("org/apache/log4j/AsyncAppender.class") != null) {
                        loggerTypes.addType(LoggerType.LOG4J);
                    }
                } catch (Throwable e) {
                    // ignore
                }
            } else if ("ch.qos.logback.classic.Logger".equals(className)) {
                try {
                    if (classLoader.getResource("ch/qos/logback/core/Appender.class") != null) {
                        loggerTypes.addType(LoggerType.LOGBACK);
                    }
                } catch (Throwable e) {
                    // ignore
                }
            } else if ("org.apache.logging.log4j.Logger".equals(className)) {
                try {
                    if (classLoader.getResource("org/apache/logging/log4j/core/LoggerContext.class") != null) {
                        loggerTypes.addType(LoggerType.LOG4J2);
                    }
                } catch (Throwable e) {
                    // ignore
                }
            }
        }
}
