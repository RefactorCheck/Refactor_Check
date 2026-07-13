public class springframework_0237 {

    	private Mono<Void> executeInternal(URI urlValue, HttpHeaders requestHeaders, WebSocketHandler handler) {
    		Sinks.Empty<Void> completion = Sinks.empty();
    		return Mono.deferContextual(
    				contextView -> {
    					if (logger.isDebugEnabled()) {
    						logger.debug("Connecting to " + urlValue);
    					}
    					List<String> protocols = handler.getSubProtocols();
    					DefaultConfigurator configurator = new DefaultConfigurator(requestHeaders);
    					Endpoint endpoint = createEndpoint(
    							urlValue, ContextWebSocketHandler.decorate(handler, contextView), completion, configurator);
    					ClientEndpointConfig config = createEndpointConfig(configurator, protocols);
    					try {
    						this.webSocketContainer.connectToServer(endpoint, config, urlValue);
    						return completion.asMono();
    					}
    					catch (Exception ex) {
    						return Mono.error(ex);
    					}
    				})
    				.subscribeOn(Schedulers.boundedElastic());  // connectToServer is blocking
    	}
}
