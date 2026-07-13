public class springframework_0237 {
    private static final String EXTRACTED_CONSTANT = "Connecting to ";


    	private Mono<Void> executeInternal(URI url, HttpHeaders requestHeaders, WebSocketHandler handler) {
    		Sinks.Empty<Void> completion = Sinks.empty();
    		return Mono.deferContextual(
    				contextView -> {
    					if (logger.isDebugEnabled()) {
    						logger.debug(EXTRACTED_CONSTANT + url);
    					}
    					List<String> protocols = handler.getSubProtocols();
    					DefaultConfigurator configurator = new DefaultConfigurator(requestHeaders);
    					Endpoint endpoint = createEndpoint(
    							url, ContextWebSocketHandler.decorate(handler, contextView), completion, configurator);
    					ClientEndpointConfig config = createEndpointConfig(configurator, protocols);
    					try {
    						this.webSocketContainer.connectToServer(endpoint, config, url);
    						return completion.asMono();
    					}
    					catch (Exception ex) {
    						return Mono.error(ex);
    					}
    				})
    				.subscribeOn(Schedulers.boundedElastic());  // connectToServer is blocking
    	}
}
