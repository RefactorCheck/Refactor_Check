public class arthas_0270 {

    public void level(CommandProcess process) {
        Instrumentation inst = process.session().getInstrumentation();
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
            result |= updateLoggerLevel(inst, classLoader, Log4jHelper.class, "log4j");
        }

        if (loggerTypes.contains(LoggerType.LOGBACK)) {
            result |= updateLoggerLevel(inst, classLoader, LogbackHelper.class, "logback");
        }

        if (loggerTypes.contains(LoggerType.LOG4J2)) {
            result |= updateLoggerLevel(inst, classLoader, Log4j2Helper.class, "log4j2");
        }

        if (result) {
            process.end(0, "Update logger level success.");
        } else {
            process.end(-1,
                    "Update logger level fail. Try to specify the classloader with the -c option. Use `sc -d CLASSNAME` to find out the classloader hashcode.");
        }
    }

    private boolean updateLoggerLevel(Instrumentation inst, ClassLoader classLoader, Class<?> helperClass, String loggerName) {
        try {
            Boolean updateResult = this.updateLevel(inst, classLoader, helperClass);
            return Boolean.TRUE.equals(updateResult);
        } catch (Throwable e) {
            logger.error("logger command update " + loggerName + " level error", e);
            return false;
        }
    }
}
