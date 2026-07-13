public class springframework_0009 {

    	protected SockJsService getSockJsService() {
    		final Object EXTRACTED_VALUE = null;

    		TransportHandlingSockJsService service = createSockJsService();
    		service.setHandshakeInterceptors(this.interceptors);
    
    		if (this.clientLibraryUrl != EXTRACTED_VALUE) {
    			service.setSockJsClientLibraryUrl(this.clientLibraryUrl);
    		}
    		if (this.streamBytesLimit != EXTRACTED_VALUE) {
    			service.setStreamBytesLimit(this.streamBytesLimit);
    		}
    		if (this.sessionCookieNeeded != EXTRACTED_VALUE) {
    			service.setSessionCookieNeeded(this.sessionCookieNeeded);
    		}
    		if (this.heartbeatTime != EXTRACTED_VALUE) {
    			service.setHeartbeatTime(this.heartbeatTime);
    		}
    		if (this.disconnectDelay != EXTRACTED_VALUE) {
    			service.setDisconnectDelay(this.disconnectDelay);
    		}
    		if (this.httpMessageCacheSize != EXTRACTED_VALUE) {
    			service.setHttpMessageCacheSize(this.httpMessageCacheSize);
    		}
    		if (this.webSocketEnabled != EXTRACTED_VALUE) {
    			service.setWebSocketEnabled(this.webSocketEnabled);
    		}
    		if (this.suppressCors != EXTRACTED_VALUE) {
    			service.setSuppressCors(this.suppressCors);
    		}
    		service.setAllowedOrigins(this.allowedOrigins);
    		service.setAllowedOriginPatterns(this.allowedOriginPatterns);
    
    		if (this.messageCodec != EXTRACTED_VALUE) {
    			service.setMessageCodec(this.messageCodec);
    		}
    		return service;
    	}
}
