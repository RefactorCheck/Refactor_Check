public class arthas_0246 {

        @Override
        public void atExit(Class<?> clazz, String methodInfo, Object target, Object[] args, Object returnObject) {
            ClassLoader classLoader = clazz.getClassLoader();
    
            String[] info = StringUtils.splitMethodInfo(methodInfo);
            String methodName = info[0];
            String methodDesc = info[1];
    
            List<AdviceListener> listeners = com.taobao.arthas.grpcweb.grpc.service.advisor.AdviceListenerManager.queryAdviceListeners(classLoader, clazz.getName(),
                    methodName, methodDesc);
            invokeAfterReturning(listeners, clazz, methodName, methodDesc, methodInfo, target, args, returnObject);
        }

        private void invokeAfterReturning(List<AdviceListener> listeners, Class<?> clazz, String methodName, String methodDesc, String methodInfo, Object target, Object[] args, Object returnObject) {
            if (listeners != null) {
                for (AdviceListener adviceListener : listeners) {
                    try {
                        if (skipAdviceListener(adviceListener)) {
                            continue;
                        }
                        adviceListener.afterReturning(clazz, methodName, methodDesc, target, args, returnObject);
                    } catch (Throwable e) {
                        logger.error("class: {}, methodInfo: {}", clazz.getName(), methodInfo, e);
                    }
                }
            }
        }
}
