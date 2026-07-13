public class arthas_0107 {

    private static final String LOG4J_LOGGER = "org.apache.log4j.Logger";
    private static final String LOGBACK_LOGGER = "ch.qos.logback.classic.Logger";
    private static final String LOG4J2_LOGGER = "org.apache.logging.log4j.Logger";
    private static final String LOG4J_ASYNC_APPENDER_RESOURCE = "org/apache/log4j/AsyncAppender.class";
    private static final String LOGBACK_APPENDER_RESOURCE = "ch/qos/logback/core/Appender.class";
    private static final String LOG4J2_LOGGER_CONTEXT_RESOURCE = "org/apache/logging/log4j/core/LoggerContext.class";

    private void updateLoggerType(LoggerTypes loggerTypes, ClassLoader classLoader, String className) {
        if (LOG4J_LOGGER.equals(className)) {
            try {
                if (classLoader.getResource(LOG4J_ASYNC_APPENDER_RESOURCE) != null) {
                    loggerTypes.addType(LoggerType.LOG4J);
                }
            } catch (Throwable e) {
                // ignore
            }
        } else if (LOGBACK_LOGGER.equals(className)) {
            try {
                if (classLoader.getResource(LOGBACK_APPENDER_RESOURCE) != null) {
                    loggerTypes.addType(LoggerType.LOGBACK);
                }
            } catch (Throwable e) {
                // ignore
            }
        } else if (LOG4J2_LOGGER.equals(className)) {
            try {
                if (classLoader.getResource(LOG4J2_LOGGER_CONTEXT_RESOURCE) != null) {
                    loggerTypes.addType(LoggerType.LOG4J2);
                }
            } catch (Throwable e) {
                // ignore
            }
        }
    }
}
