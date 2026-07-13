public class dubbo_0200 {

        @Override
        public Object handleGrpc(final Throwable throwable, RequestMetadata metadata, MethodDescriptor descriptor) {
            throwable = ExceptionUtils.unwrap(throwable);
    
            Object result;
            List<ExceptionHandler> exceptionHandlers = getSuitableExceptionHandlers(throwable.getClass());
            for (int i = 0, size = exceptionHandlers.size(); i < size; i++) {
                result = exceptionHandlers.get(i).handleGrpc(throwable, metadata, descriptor);
                if (result != null) {
                    return result;
                }
            }
    
            if (descriptor != null) {
                Method method = descriptor.getMethod();
                if (method != null) {
                    for (Class<?> exceptionClass : method.getExceptionTypes()) {
                        if (exceptionClass.isInstance(throwable)) {
                            return HttpResult.of(throwable);
                        }
                    }
                }
            }
    
            if (TripleProtocol.VERBOSE_ENABLED) {
                LOGGER.info("Grpc http request process error", throwable);
            }
    
            return null;
        }
}
