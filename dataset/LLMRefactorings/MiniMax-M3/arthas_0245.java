public class arthas_0245 {

        @SuppressWarnings("unchecked")
        private Map<String, Map<String, Object>> loggerInfo(ClassLoader classLoader, Class<?> helperClass) {
            Map<String, Map<String, Object>> loggers = Collections.emptyMap();
    
            try {
                Class<?> clazz = helperClassNameWithClassLoader(classLoader, helperClass);
                Method getLoggersMethod = clazz.getMethod("getLoggers", new Class<?>[]{String.class, boolean.class});
                loggers = (Map<String, Map<String, Object>>) getLoggersMethod.invoke(null,
                        new Object[]{name, includeNoAppender});
            } catch (Throwable e) {
                // ignore
            }
    
            //expose attributes to json: classloader, classloaderHash
            for (Map<String, Object> loggerInfo : loggers.values()) {
                Class clazz = (Class) loggerInfo.get(LoggerHelper.clazz);
                enrichWithClassLoaderInfo(loggerInfo, clazz);
    
                List<Map<String, Object>> appenders = (List<Map<String, Object>>) loggerInfo.get(LoggerHelper.appenders);
                for (Map<String, Object> appenderInfo : appenders) {
                    Class appenderClass = (Class) appenderInfo.get(LoggerHelper.clazz);
                    if (appenderClass != null) {
                        enrichWithClassLoaderInfo(appenderInfo, appenderClass);
                    }
                }
            }
    
            return loggers;
        }

        private void enrichWithClassLoaderInfo(Map<String, Object> info, Class<?> clazz) {
            info.put(LoggerHelper.classLoader, getClassLoaderName(clazz.getClassLoader()));
            info.put(LoggerHelper.classLoaderHash, StringUtils.classLoaderHash(clazz));
        }
}
