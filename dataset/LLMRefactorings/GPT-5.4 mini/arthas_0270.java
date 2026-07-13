public class arthas_0270 {
    private Instrumentation inst;


        public void level(CommandProcess process) {
            inst = process.session().getInstrumentation();
            boolean result = false;
    
            // 如果不指定 classloader，则默认用 SystemClassLoader
            ClassLoader classLoader = ClassLoader.getSystemClassLoader();
            if (hashCode != null) {
                classLoader = ClassLoaderUtils.getClassLoader(inst, hashCode);
                if (classLoader == null) {
                    process.end(-1, "Can not find classloader by hashCode: " + hashCode + ".");
                    return;
                }
            }
    
            LoggerTypes loggerTypes = findLoggerTypes(process.session().getInstrumentation(), classLoader);
            if (loggerTypes.contains(LoggerType.LOG4J)) {
                try {
                    Boolean updateResult = this.updateLevel(inst, classLoader, Log4jHelper.class);
                    if (Boolean.TRUE.equals(updateResult)) {
                        result = true;
                    }
                } catch (Throwable e) {
                    logger.error("logger command update log4j level error", e);
                }
            }
    
            if (loggerTypes.contains(LoggerType.LOGBACK)) {
                try {
                    Boolean updateResult = this.updateLevel(inst, classLoader, LogbackHelper.class);
                    if (Boolean.TRUE.equals(updateResult)) {
                        result = true;
                    }
                } catch (Throwable e) {
                    logger.error("logger command update logback level error", e);
                }
            }
    
            if (loggerTypes.contains(LoggerType.LOG4J2)) {
                try {
                    Boolean updateResult = this.updateLevel(inst, classLoader, Log4j2Helper.class);
                    if (Boolean.TRUE.equals(updateResult)) {
                        result = true;
                    }
                } catch (Throwable e) {
                    logger.error("logger command update log4j2 level error", e);
                }
            }
    
            if (result) {
                process.end(0, "Update logger level success.");
            } else {
                process.end(-1,
                        "Update logger level fail. Try to specify the classloader with the -c option. Use `sc -d CLASSNAME` to find out the classloader hashcode.");
            }
        }
}
