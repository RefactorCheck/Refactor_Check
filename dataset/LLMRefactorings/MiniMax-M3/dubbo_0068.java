public class dubbo_0068 {

        @Override
        public <T> T getProxy(Invoker<T> invoker, Class<?>[] interfaces) {
            try {
                return doGetProxy(invoker, interfaces);
            } catch (Throwable throwable) {
                return fallbackToJdkProxy(invoker, interfaces, throwable);
            }
        }

        private <T> T fallbackToJdkProxy(Invoker<T> invoker, Class<?>[] interfaces, Throwable throwable) {
            String factoryName = getClass().getSimpleName();
            try {
                T proxy = jdkProxyFactory.getProxy(invoker, interfaces);
                logger.error(
                        PROXY_FAILED,
                        "",
                        "",
                        "Failed to generate proxy by " + factoryName + " failed. Fallback to use JDK proxy success. "
                                + "Interfaces: " + Arrays.toString(interfaces),
                        throwable);
                return proxy;
            } catch (Throwable fromJdk) {
                logger.error(
                        PROXY_FAILED,
                        "",
                        "",
                        "Failed to generate proxy by " + factoryName
                                + " failed. Fallback to use JDK proxy is also failed. " + "Interfaces: "
                                + Arrays.toString(interfaces) + " Javassist Error.",
                        throwable);
                logger.error(
                        PROXY_FAILED,
                        "",
                        "",
                        "Failed to generate proxy by " + factoryName
                                + " failed. Fallback to use JDK proxy is also failed. " + "Interfaces: "
                                + Arrays.toString(interfaces) + " JDK Error.",
                        fromJdk);
                throw throwable;
            }
        }
}
