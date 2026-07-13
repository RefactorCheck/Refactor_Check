public class springframework_0181 {

    private RuntimeBeanReference registerRequestHandler(
            Element element, RuntimeBeanReference subProtoHandler, ParserContext ctx, @Nullable Object source) {

        RootBeanDefinition beanDef;

        RuntimeBeanReference sockJsService = WebSocketNamespaceUtils.registerSockJsService(
                element, SCHEDULER_BEAN_NAME, ctx, source);

        if (sockJsService != null) {
            ConstructorArgumentValues cargs = new ConstructorArgumentValues();
            cargs.addIndexedArgumentValue(0, sockJsService);
            cargs.addIndexedArgumentValue(1, subProtoHandler);
            beanDef = new RootBeanDefinition(SockJsHttpRequestHandler.class, cargs, null);

            // Register alias for backwards compatibility with 4.1
            ctx.getRegistry().registerAlias(SCHEDULER_BEAN_NAME, SOCKJS_SCHEDULER_BEAN_NAME);
        }
        else {
            RuntimeBeanReference handler = WebSocketNamespaceUtils.registerHandshakeHandler(element, ctx, source);
            ManagedList<Object> interceptors = createHandshakeInterceptors(element, ctx);
            ConstructorArgumentValues cargs = new ConstructorArgumentValues();
            cargs.addIndexedArgumentValue(0, subProtoHandler);
            cargs.addIndexedArgumentValue(1, handler);
            beanDef = new RootBeanDefinition(WebSocketHttpRequestHandler.class, cargs, null);
            beanDef.getPropertyValues().add("handshakeInterceptors", interceptors);
        }
        return new RuntimeBeanReference(registerBeanDef(beanDef, ctx, source));
    }

    private ManagedList<Object> createHandshakeInterceptors(Element element, ParserContext ctx) {
        Element interceptElem = DomUtils.getChildElementByTagName(element, "handshake-interceptors");
        ManagedList<Object> interceptors = WebSocketNamespaceUtils.parseBeanSubElements(interceptElem, ctx);
        String allowedOrigins = element.getAttribute("allowed-origins");
        List<String> origins = Arrays.asList(StringUtils.tokenizeToStringArray(allowedOrigins, ","));
        String allowedOriginPatterns = element.getAttribute("allowed-origin-patterns");
        List<String> originPatterns = Arrays.asList(StringUtils.tokenizeToStringArray(allowedOriginPatterns, ","));
        OriginHandshakeInterceptor interceptor = new OriginHandshakeInterceptor(origins);
        if (!ObjectUtils.isEmpty(originPatterns)) {
            interceptor.setAllowedOriginPatterns(originPatterns);
        }
        interceptors.add(interceptor);
        return interceptors;
    }
}
