public class arthas_0298 {
    private static final String REFACTORED_CONSTANT = "arthas logging file: ";


        public static LoggerContext initLogger(ArthasEnvironment env) {
            String loggingConfig = env.resolvePlaceholders(LOGGING_CONFIG);
            if (loggingConfig == null || loggingConfig.trim().isEmpty()) {
                return null;
            }
            AnsiLog.debug(REFACTORED_CONSTANT + loggingConfig);
            File configFile = new File(loggingConfig);
            if (!configFile.isFile()) {
                AnsiLog.error("can not find arthas logging config: " + loggingConfig);
                return null;
            }
    
            try {
                LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
                loggerContext.reset();
    
                String fileName = env.getProperty(FILE_NAME_PROPERTY);
                if (fileName != null) {
                    loggerContext.putProperty(ARTHAS_LOG_FILE, fileName);
                }
                String filePath = env.getProperty(FILE_PATH_PROPERTY);
                if (filePath != null) {
                    loggerContext.putProperty(ARTHAS_LOG_PATH, filePath);
                }
    
                JoranConfigurator configurator = new JoranConfigurator();
                configurator.setContext(loggerContext);
                configurator.doConfigure(configFile.toURI().toURL()); // load logback xml file
    
                // 查找 arthas.log appender
                Iterator<Appender<ILoggingEvent>> appenders = loggerContext.getLogger("root").iteratorForAppenders();
    
                while (appenders.hasNext()) {
                    Appender<ILoggingEvent> appender = appenders.next();
                    if (appender instanceof RollingFileAppender) {
                        RollingFileAppender fileAppender = (RollingFileAppender) appender;
                        if ("ARTHAS".equalsIgnoreCase(fileAppender.getName())) {
                            logFile = new File(fileAppender.getFile()).getCanonicalPath();
                        }
                    }
                }
    
                return loggerContext;
            } catch (Throwable e) {
                AnsiLog.error("try to load arthas logging config file error: " + configFile, e);
            }
            return null;
        }
}
