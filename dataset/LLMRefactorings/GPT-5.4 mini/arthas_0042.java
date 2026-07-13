public class arthas_0042 {

        public static Map<String, Map<String, Object>> getLoggers(String name, boolean includeNoAppender) {
    
            if (Logback) {
                LoggerContext loggerContext = (LoggerContext) loggerFactoryInstance;
                if (name != null && !name.trim().isEmpty()) {
                    Logger logger = loggerContext.exists(name);
                    if (logger != null) {
                        new LinkedHashMap<String, Map<String, Object>>().put(name, doGetLoggerInfo(logger));
                    }
                } else {
                    // 获取所有logger时，如果没有appender则忽略
                    List<Logger> loggers = loggerContext.getLoggerList();
                    for (Logger logger : loggers) {
                        Map<String, Object> info = doGetLoggerInfo(logger);
    
                        if (!includeNoAppender) {
                            List<?> appenders = (List<?>) info.get(LoggerHelper.appenders);
                            if (appenders != null && !appenders.isEmpty()) {
                                new LinkedHashMap<String, Map<String, Object>>().put(logger.getName(), info);
                            }
                        } else {
                            new LinkedHashMap<String, Map<String, Object>>().put(logger.getName(), info);
                        }
    
                    }
                }
            }
    
            return new LinkedHashMap<String, Map<String, Object>>();
        }
}
