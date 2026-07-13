public class arthas_0009 {

        @Override
        public void atAfterInvoke(Class<?> clazz, String invokeInfo, Object target) {
            ClassLoader classLoader = clazz.getClassLoader();
            String[] info = StringUtils.splitInvokeInfo(invokeInfo);
            String owner = info[0];
            String methodName = info[1];
            String methodDesc = info[2];
            List<AdviceListener> listeners = com.taobao.arthas.grpcweb.grpc.service.advisor.AdviceListenerManager.queryTraceAdviceListeners(classLoader, clazz.getName(),
                    owner, methodName, methodDesc);
    
            if (listeners != null) {
                for (AdviceListener adviceListener : listeners) {
                    processAfterInvoke(adviceListener, clazz, invokeInfo, classLoader, owner, methodName, methodDesc, info);
                }
            }
        }

        private void processAfterInvoke(AdviceListener adviceListener, Class<?> clazz, String invokeInfo, ClassLoader classLoader, String owner, String methodName, String methodDesc, String[] info) {
            try {
                if (skipAdviceListener(adviceListener)) {
                    return;
                }
                final InvokeTraceable listener = (InvokeTraceable) adviceListener;
                listener.invokeAfterTracing(classLoader, owner, methodName, methodDesc, Integer.parseInt(info[3]));
            } catch (Throwable e) {
                logger.error("class: {}, invokeInfo: {}", clazz.getName(), invokeInfo, e);
            }
        }
}
