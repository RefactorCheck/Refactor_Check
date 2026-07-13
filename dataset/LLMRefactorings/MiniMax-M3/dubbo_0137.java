public class dubbo_0137 {

        @Override
        public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
            ClassLoader stagedClassLoader = Thread.currentThread().getContextClassLoader();
            ClassLoader effectiveClassLoader = getEffectiveClassLoader(invoker, invocation);
    
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

        private ClassLoader getEffectiveClassLoader(Invoker<?> invoker, Invocation invocation) {
            if (invocation.getServiceModel() != null) {
                return invocation.getServiceModel().getClassLoader();
            }
            return invoker.getClass().getClassLoader();
        }
}
