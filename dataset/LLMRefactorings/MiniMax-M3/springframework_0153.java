public class springframework_0153 {

    private void registerWebSocketMessageBrokerStats(RootBeanDefinition broker, RuntimeBeanReference inChannel,
            RuntimeBeanReference outChannel, ParserContext context, @Nullable Object source) {

        RootBeanDefinition beanDef = new RootBeanDefinition(WebSocketMessageBrokerStats.class);

        RuntimeBeanReference webSocketHandler = new RuntimeBeanReference(WEB_SOCKET_HANDLER_BEAN_NAME);
        beanDef.getPropertyValues().add("subProtocolWebSocketHandler", webSocketHandler);

        if (StompBrokerRelayMessageHandler.class == broker.getBeanClass()) {
            beanDef.getPropertyValues().add("stompBrokerRelay", broker);
        }
        addExecutorIfDefined(beanDef, inChannel, "inboundChannelExecutor", context);
        addExecutorIfDefined(beanDef, outChannel, "outboundChannelExecutor", context);
        Object scheduler = WebSocketNamespaceUtils.registerScheduler(SCHEDULER_BEAN_NAME, context, source);
        beanDef.getPropertyValues().add("sockJsTaskScheduler", scheduler);

        registerBeanDefByName("webSocketMessageBrokerStats", beanDef, context, source);
    }

    private void addExecutorIfDefined(RootBeanDefinition beanDef, RuntimeBeanReference channel,
            String propertyName, ParserContext context) {
        String name = channel.getBeanName() + "Executor";
        if (context.getRegistry().containsBeanDefinition(name)) {
            beanDef.getPropertyValues().add(propertyName, context.getRegistry().getBeanDefinition(name));
        }
    }
}
