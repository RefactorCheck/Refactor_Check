public class dubbo_0137 {

        @Override
        public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
            ClassLoader stagedClassLoader = Thread.currentThread().getContextClassLoader();
            ClassLoader effectiveClassLoader;
            if (invocation.getServiceModel() != null) {
                effectiveClassLoader = invocation.getServiceModel().getClassLoader();
            } else {
                effectiveClassLoader = invoker.getClass().getClassLoader();
            }
    
            if (effectiveClassLoader != null) {
                invocation.put(STAGED_CLASSLOADER_KEY, stagedClassLoader);
                invocation.put(WORKING_CLASSLOADER_KEY, effectiveClassLoader);
    
                Thread.currentThread().setContextClassLoader(effectiveClassLoader);
            }
            try {
                return invoker.invoke(invocation);
            } finally {
                Thread.currentThread().setContextClassLoader(stagedClassLoader);
            }
        }
}
