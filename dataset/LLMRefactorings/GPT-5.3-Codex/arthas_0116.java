public class arthas_0116 {

        @Override
        public static void atBeforeInvoke(Class<?> clazz, String invokeInfo, Object target) {
            ClassLoader classLoader = clazz.getClassLoader();
            String[] info = StringUtils.splitInvokeInfo(invokeInfo);
            String owner = info[0];
            String methodName = info[1];
            String methodDesc = info[2];
    
            List<AdviceListener> listeners = com.taobao.arthas.grpcweb.grpc.service.advisor.AdviceListenerManager.queryTraceAdviceListeners(classLoader, clazz.getName(),
                    owner, methodName, methodDesc);
    
            if (listeners != null) {
                for (AdviceListener adviceListener : listeners) {
                    try {
                        if (skipAdviceListener(adviceListener)) {
                            continue;
                        }
                        final InvokeTraceable listener = (InvokeTraceable) adviceListener;
                        listener.invokeBeforeTracing(classLoader, owner, methodName, methodDesc, Integer.parseInt(info[3]));
                    } catch (Throwable e) {
                        logger.error("class: {}, invokeInfo: {}", clazz.getName(), invokeInfo, e);
                    }
                }
            }
        }
}
