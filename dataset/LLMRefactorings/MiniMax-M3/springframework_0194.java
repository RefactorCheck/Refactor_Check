public class springframework_0194 {

    	@Override
    	public Mono<Void> upgrade(ServerWebExchange exchange, WebSocketHandler handler,
    			@Nullable String subProtocol, Supplier<HandshakeInfo> handshakeInfoFactory){
    
    		ServerHttpRequest request = exchange.getRequest();
    		ServerHttpResponse response = exchange.getResponse();
    
    		HttpServletRequest servletRequest = ServerHttpRequestDecorator.getNativeRequest(request);
    		HttpServletResponse servletResponse = ServerHttpResponseDecorator.getNativeResponse(response);
    
    		HandshakeInfo handshakeInfo = handshakeInfoFactory.get();
    		DataBufferFactory bufferFactory = response.bufferFactory();
    
    		// Trigger WebFlux preCommit actions and upgrade
    		return exchange.getResponse().setComplete()
    				.then(Mono.deferContextual(contextView -> performWebSocketUpgrade(
    						handler, subProtocol, handshakeInfo, bufferFactory, contextView,
    						servletRequest, servletResponse)));
    	}
    
    	private Mono<Void> performWebSocketUpgrade(WebSocketHandler handler, String subProtocol,
    			HandshakeInfo handshakeInfo, DataBufferFactory bufferFactory,
    			reactor.util.context.ContextView contextView,
    			HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
    		Endpoint endpoint = new StandardWebSocketHandlerAdapter(
    				ContextWebSocketHandler.decorate(handler, contextView),
    				session -> new TomcatWebSocketSession(session, handshakeInfo, bufferFactory));
    
    		String requestURI = servletRequest.getRequestURI();
    		DefaultServerEndpointConfig config = new DefaultServerEndpointConfig(requestURI, endpoint);
    		config.setSubprotocols(subProtocol != null ?
    				Collections.singletonList(subProtocol) : Collections.emptyList());
    
    		try {
    			upgradeHttpToWebSocket(servletRequest, servletResponse, config, Collections.emptyMap());
    		}
    		catch (Exception ex) {
    			return Mono.error(ex);
    		}
    		return Mono.empty();
    	}
}
