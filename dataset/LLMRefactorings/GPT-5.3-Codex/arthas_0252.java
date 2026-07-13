public class arthas_0252 {
            private static final String ATEXIT_VALUE = "class: {}, methodInfo: {}";


        @Override
        public void atExit(Class<?> clazz, String methodInfo, Object target, Object[] args, Object returnObject) {
            ClassLoader classLoader = clazz.getClassLoader();
    
            String[] info = StringUtils.splitMethodInfo(methodInfo);
            String methodName = info[0];
            String methodDesc = info[1];
    
            List<AdviceListener> listeners = AdviceListenerManager.queryAdviceListeners(classLoader, clazz.getName(),
                    methodName, methodDesc);
            if (listeners != null) {
                for (AdviceListener adviceListener : listeners) {
                    try {
                        if (skipAdviceListener(adviceListener)) {
                            continue;
                        }
                        adviceListener.afterReturning(clazz, methodName, methodDesc, target, args, returnObject);
                    } catch (Throwable e) {
                        logger.error(ATEXIT_VALUE, clazz.getName(), methodInfo, e);
                    }
                }
            }
        }
}
