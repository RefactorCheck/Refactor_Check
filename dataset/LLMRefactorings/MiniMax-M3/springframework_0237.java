public class springframework_0237 {

    private Mono<Void> executeInternal(URI url, HttpHeaders requestHeaders, WebSocketHandler handler) {
        Sinks.Empty<Void> completion = Sinks.empty();
        return Mono.deferContextual(contextView -> connect(contextView, url, requestHeaders, handler, completion))
                .subscribeOn(Schedulers.boundedElastic());
    }

    private Mono<Void> connect(ContextView contextView, URI url, HttpHeaders requestHeaders,
                                WebSocketHandler handler, Sinks.Empty<Void> completion) {
        if (logger.isDebugEnabled()) {
            logger.debug("Connecting to " + url);
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
    }
}
