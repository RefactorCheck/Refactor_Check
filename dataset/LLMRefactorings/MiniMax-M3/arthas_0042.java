public class arthas_0042 {

    public static Map<String, Map<String, Object>> getLoggers(String name, boolean includeNoAppender) {
        Map<String, Map<String, Object>> loggerInfoMap = new LinkedHashMap<String, Map<String, Object>>();

        if (Logback) {
            LoggerContext loggerContext = (LoggerContext) loggerFactoryInstance;
            if (name != null && !name.trim().isEmpty()) {
                Logger logger = loggerContext.exists(name);
                if (logger != null) {
                    loggerInfoMap.put(name, doGetLoggerInfo(logger));
                }
            } else {
                collectLoggers(loggerContext, includeNoAppender, loggerInfoMap);
            }
        }

        return loggerInfoMap;
    }

    private static void collectLoggers(LoggerContext loggerContext, boolean includeNoAppender, Map<String, Map<String, Object>> loggerInfoMap) {
        List<Logger> loggers = loggerContext.getLoggerList();
        for (Logger logger : loggers) {
            Map<String, Object> info = doGetLoggerInfo(logger);

            if (!includeNoAppender) {
                List<?> appenders = (List<?>) info.get(LoggerHelper.appenders);
                if (appenders != null && !appenders.isEmpty()) {
                    loggerInfoMap.put(logger.getName(), info);
                }
            } else {
                loggerInfoMap.put(logger.getName(), info);
            }
        }
    }
}
