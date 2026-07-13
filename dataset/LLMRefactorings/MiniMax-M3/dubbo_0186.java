public class dubbo_0186 {

    private void logError(Throwable t) {
        Supplier<String> msg = this::buildErrorMessage;
        Throwable th = ExceptionUtils.unwrap(t);
        LOGGER.msg(msg).log(exceptionCustomizerWrapper.resolveLogLevel(th), th);
    }

    private String buildErrorMessage() {
        StringBuilder sb = new StringBuilder(128);
        sb.append("An error occurred while processing the http request with ")
                .append(getClass().getSimpleName())
                .append(", ")
                .append(httpMetadata);
        if (TripleProtocol.VERBOSE_ENABLED) {
            sb.append(", headers=").append(httpMetadata.headers());
        }
        if (context != null) {
            MethodDescriptor md = context.getMethodDescriptor();
            if (md != null) {
                sb.append(", method=").append(MethodUtils.toShortString(md));
            }
            if (TripleProtocol.VERBOSE_ENABLED) {
                Invoker<?> invoker = context.getInvoker();
                if (invoker != null) {
                    URL url = invoker.getUrl();
                    Object service = url.getServiceModel().getProxyObject();
                    sb.append(", service=")
                            .append(service.getClass().getSimpleName())
                            .append('@')
                            .append(Integer.toHexString(System.identityHashCode(service)))
                            .append(", url='")
                            .append(url)
                            .append('\'');
                }
            }
        }
        return sb.toString();
    }
}
