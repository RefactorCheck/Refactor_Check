public class arthas_0252 {

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
                    invokeAfterReturning(adviceListener, clazz, methodName, methodDesc, methodInfo, target, args, returnObject);
                }
            }
        }

        private void invokeAfterReturning(AdviceListener adviceListener, Class<?> clazz, String methodName,
                String methodDesc, String methodInfo, Object target, Object[] args, Object returnObject) {
            try {
                if (skipAdviceListener(adviceListener)) {
                    return;
                }
                adviceListener.afterReturning(clazz, methodName, methodDesc, target, args, returnObject);
            } catch (Throwable e) {
                logger.error("class: {}, methodInfo: {}", clazz.getName(), methodInfo, e);
            }
        }
}
