public class springframework_0143 {

    protected final void addDefaultHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers,
            ContentNegotiationManager mvcContentNegotiationManager) {

        ExceptionHandlerExceptionResolver exceptionHandlerResolver =
                configureExceptionHandlerExceptionResolver(mvcContentNegotiationManager);
        exceptionResolvers.add(exceptionHandlerResolver);

        ResponseStatusExceptionResolver responseStatusResolver = new ResponseStatusExceptionResolver();
        responseStatusResolver.setMessageSource(this.applicationContext);
        exceptionResolvers.add(responseStatusResolver);

        exceptionResolvers.add(new DefaultHandlerExceptionResolver());
    }

    private ExceptionHandlerExceptionResolver configureExceptionHandlerExceptionResolver(
            ContentNegotiationManager mvcContentNegotiationManager) {

        ExceptionHandlerExceptionResolver exceptionHandlerResolver = createExceptionHandlerExceptionResolver();
        exceptionHandlerResolver.setContentNegotiationManager(mvcContentNegotiationManager);
        exceptionHandlerResolver.setMessageConverters(getMessageConverters());
        exceptionHandlerResolver.setCustomArgumentResolvers(getArgumentResolvers());
        exceptionHandlerResolver.setCustomReturnValueHandlers(getReturnValueHandlers());
        exceptionHandlerResolver.setErrorResponseInterceptors(getErrorResponseInterceptors());
        if (JACKSON_PRESENT || JACKSON_2_PRESENT || KOTLIN_SERIALIZATION_PRESENT) {
            List<ResponseBodyAdvice<?>> responseBodyAdvices = new ArrayList<>(2);
            if (JACKSON_PRESENT || JACKSON_2_PRESENT) {
                responseBodyAdvices.add(new JsonViewResponseBodyAdvice());
            }
            if (KOTLIN_SERIALIZATION_PRESENT) {
                responseBodyAdvices.add(new KotlinResponseBodyAdvice());
            }
            exceptionHandlerResolver.setResponseBodyAdvice(responseBodyAdvices);
        }
        if (this.applicationContext != null) {
            exceptionHandlerResolver.setApplicationContext(this.applicationContext);
        }
        exceptionHandlerResolver.afterPropertiesSet();
        return exceptionHandlerResolver;
    }
}
