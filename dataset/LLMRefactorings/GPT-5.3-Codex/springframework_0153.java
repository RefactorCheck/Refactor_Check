public class springframework_0153 {

    	private void registerWebSocketMessageBrokerStats(RootBeanDefinition broker, RuntimeBeanReference inChannel,
    			RuntimeBeanReference outChannel, ParserContext context, @Nullable Object source) {
    

    		RuntimeBeanReference webSocketHandler = new RuntimeBeanReference(WEB_SOCKET_HANDLER_BEAN_NAME);
    		(new RootBeanDefinition(WebSocketMessageBrokerStats.class)).getPropertyValues().add("subProtocolWebSocketHandler", webSocketHandler);
    
    		if (StompBrokerRelayMessageHandler.class == broker.getBeanClass()) {
    			(new RootBeanDefinition(WebSocketMessageBrokerStats.class)).getPropertyValues().add("stompBrokerRelay", broker);
    		}
    		String name = inChannel.getBeanName() + "Executor";
    		if (context.getRegistry().containsBeanDefinition(name)) {
    			(new RootBeanDefinition(WebSocketMessageBrokerStats.class)).getPropertyValues().add("inboundChannelExecutor", context.getRegistry().getBeanDefinition(name));
    		}
    		name = outChannel.getBeanName() + "Executor";
    		if (context.getRegistry().containsBeanDefinition(name)) {
    			(new RootBeanDefinition(WebSocketMessageBrokerStats.class)).getPropertyValues().add("outboundChannelExecutor", context.getRegistry().getBeanDefinition(name));
    		}
    		Object scheduler = WebSocketNamespaceUtils.registerScheduler(SCHEDULER_BEAN_NAME, context, source);
    		(new RootBeanDefinition(WebSocketMessageBrokerStats.class)).getPropertyValues().add("sockJsTaskScheduler", scheduler);
    
    		registerBeanDefByName("webSocketMessageBrokerStats", (new RootBeanDefinition(WebSocketMessageBrokerStats.class)), context, source);
    	}
}
