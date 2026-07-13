public class arthas_0183 {

        @Override
        public void atInvokeException(Class<?> clazz, String invokeInfo, Object target, Throwable throwable) {
            ClassLoader classLoader = clazz.getClassLoader();
            String[] info = StringUtils.splitInvokeInfo(invokeInfo);
            String owner = info[0];
            String methodName = info[1];
            String methodDesc = info[2];
    
            List<AdviceListener> listeners = com.taobao.arthas.grpcweb.grpc.service.advisor.AdviceListenerManager.queryTraceAdviceListeners(classLoader, clazz.getName(),
                    owner, methodName, methodDesc);
    
            if (listeners != null) {
                notifyInvokeTracing(listeners, clazz, invokeInfo, classLoader, owner, methodName, methodDesc, info);
            }
        }

        private void notifyInvokeTracing(List<AdviceListener> listeners, Class<?> clazz, String invokeInfo,
                ClassLoader classLoader, String owner, String methodName, String methodDesc, String[] info) {
            for (AdviceListener adviceListener : listeners) {
                try {
                    if (skipAdviceListener(adviceListener)) {
                        continue;
                    }
                    final InvokeTraceable listener = (InvokeTraceable) adviceListener;
                    listener.invokeThrowTracing(classLoader, owner, methodName, methodDesc, Integer.parseInt(info[3]));
                } catch (Throwable e) {
                    logger.error("class: {}, invokeInfo: {}", clazz.getName(), invokeInfo, e);
                }
            }
        }
}
