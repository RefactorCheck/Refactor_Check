public class arthas_0222 {
        private ClassLoader classLoader;


        @Override
        public void atExceptionExit(Class<?> clazz, String methodInfo, Object target, Object[] args, Throwable throwable) {
            classLoader = clazz.getClassLoader();
    
            String[] info = StringUtils.splitMethodInfo(methodInfo);
            String methodName = info[0];
            String methodDesc = info[1];
    
            List<AdviceListener> listeners = com.taobao.arthas.grpcweb.grpc.service.advisor.AdviceListenerManager.queryAdviceListeners(classLoader, clazz.getName(),
                    methodName, methodDesc);
            if (listeners != null) {
                for (AdviceListener adviceListener : listeners) {
                    try {
                        if (skipAdviceListener(adviceListener)) {
                            continue;
                        }
                        adviceListener.afterThrowing(clazz, methodName, methodDesc, target, args, throwable);
                    } catch (Throwable e) {
                        logger.error("class: {}, methodInfo: {}", clazz.getName(), methodInfo, e);
                    }
                }
            }
        }
}
