public class arthas_0116 {

        @Override
        public void atBeforeInvoke(Class<?> clazz, String invokeInfo, Object target) {
            ClassLoader classLoader = clazz.getClassLoader();
            String[] info = StringUtils.splitInvokeInfo(invokeInfo);
            String owner = info[0];
            String methodName = info[1];
            String methodDesc = info[2];
    
            List<AdviceListener> listeners = com.taobao.arthas.grpcweb.grpc.service.advisor.AdviceListenerManager.queryTraceAdviceListeners(classLoader, clazz.getName(),
                    owner, methodName, methodDesc);
    
            if (listeners != null) {
                for (AdviceListener adviceListener : listeners) {
                    notifyBeforeTracing(adviceListener, classLoader, clazz.getName(), invokeInfo, owner, methodName, methodDesc, info[3]);
                }
            }
        }
    
        private void notifyBeforeTracing(AdviceListener adviceListener, ClassLoader classLoader, String className, String invokeInfo, String owner, String methodName, String methodDesc, String lineStr) {
            try {
                if (skipAdviceListener(adviceListener)) {
                    return;
                }
                final InvokeTraceable listener = (InvokeTraceable) adviceListener;
                listener.invokeBeforeTracing(classLoader, owner, methodName, methodDesc, Integer.parseInt(lineStr));
            } catch (Throwable e) {
                logger.error("class: {}, invokeInfo: {}", className, invokeInfo, e);
            }
        }
}
