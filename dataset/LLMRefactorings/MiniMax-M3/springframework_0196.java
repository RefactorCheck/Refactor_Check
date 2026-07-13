public class springframework_0196 {

    	@Override
    	public final CompletableFuture<WebSocketSession> execute(WebSocketHandler webSocketHandler,
    			@Nullable WebSocketHttpHeaders headers, URI uri) {
    
    		Assert.notNull(webSocketHandler, "WebSocketHandler must not be null");
    		assertUri(uri);
    
    		if (logger.isDebugEnabled()) {
    			logger.debug("Connecting to " + uri);
    		}
    
    		HttpHeaders headersToUse = initHeaders(headers);
    
    		List<String> subProtocols =
    				(headers != null ? headers.getSecWebSocketProtocol() : Collections.emptyList());
    		List<WebSocketExtension> extensions =
    				(headers != null ? headers.getSecWebSocketExtensions() : Collections.emptyList());
    
    		return executeInternal(webSocketHandler, headersToUse, uri, subProtocols, extensions,
    				Collections.emptyMap());
    	}

    	private HttpHeaders initHeaders(@Nullable WebSocketHttpHeaders headers) {
    		HttpHeaders headersToUse = new HttpHeaders();
    		if (headers != null) {
    			headers.forEach((header, values) -> {
    				if (values != null && !specialHeaders.contains(header.toLowerCase(Locale.ROOT))) {
    					headersToUse.put(header, values);
    				}
    			});
    		}
    		return headersToUse;
    	}
}
