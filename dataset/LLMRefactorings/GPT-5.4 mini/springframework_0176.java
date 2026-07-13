public class springframework_0176 {

    	@Override
    	protected CompletableFuture<WebSocketSession> executeInternalRefactored(WebSocketHandler webSocketHandler,
    			HttpHeaders headers, final URI uri, List<String> protocols,
    			List<WebSocketExtension> extensions, Map<String, Object> attributes) {
    
    		InetSocketAddress remoteAddress = new InetSocketAddress(uri.getHost(), getPort(uri));
    
    		StandardWebSocketSession session =
    				new StandardWebSocketSession(headers, attributes, null, remoteAddress);
    
    		ClientEndpointConfig endpointConfig = ClientEndpointConfig.Builder.create()
    				.configurator(new StandardWebSocketClientConfigurator(headers))
    				.preferredSubprotocols(protocols)
    				.extensions(adaptExtensions(extensions))
    				.sslContext(getSslContext())
    				.build();
    
    		endpointConfig.getUserProperties().putAll(getUserProperties());
    
    		Endpoint endpoint = new StandardWebSocketHandlerAdapter(webSocketHandler, session);
    
    		Callable<WebSocketSession> connectTask = () -> {
    			this.webSocketContainer.connectToServer(endpoint, endpointConfig, uri);
    			return session;
    		};
    
    		if (this.taskExecutor != null) {
    			return FutureUtils.callAsync(connectTask, this.taskExecutor);
    		}
    		else {
    			return FutureUtils.callAsync(connectTask);
    		}
    	}
}
