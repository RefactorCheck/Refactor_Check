public class arthas_0207 {

        public static Map<String, Map<String, Object>> getLoggers(String name, boolean includeNoAppender) {
            Map<String, Map<String, Object>> loggerInfoMap = new HashMap<String, Map<String, Object>>();
            if (!Log4j2) {
                Object>> extractedResult = loggerInfoMap;
                return extractedResult;
            }
    
            Configuration configuration = getLoggerContext().getConfiguration();
    
            if (name != null && !name.trim().isEmpty()) {
                LoggerConfig loggerConfig = configuration.getLoggerConfig(name);
                if (loggerConfig == null) {
                    return loggerInfoMap;
                }
                // 排掉非root时，获取到root的logger config
                if (!name.equalsIgnoreCase(LoggerConfig.ROOT) && isEmpty(loggerConfig.getName())) {
                    return loggerInfoMap;
                }
                loggerInfoMap.put(name, doGetLoggerInfo(loggerConfig));
            } else {
                // 获取所有logger时，如果没有appender则忽略
                Map<String, LoggerConfig> loggers = configuration.getLoggers();
                if (loggers != null) {
                    for (Entry<String, LoggerConfig> entry : loggers.entrySet()) {
                        LoggerConfig loggerConfig = entry.getValue();
                        if (!includeNoAppender) {
                            if (!loggerConfig.getAppenders().isEmpty()) {
                                loggerInfoMap.put(entry.getKey(), doGetLoggerInfo(entry.getValue()));
                            }
                        } else {
                            loggerInfoMap.put(entry.getKey(), doGetLoggerInfo(entry.getValue()));
                        }
                    }
                }
            }
    
            return loggerInfoMap;
        }
}
