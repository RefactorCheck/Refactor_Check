public class springframework_0119 {

    	public static SockJsServiceRegistration withSockJS() {
    		this.sockJsServiceRegistration = new SockJsServiceRegistration();
    		HandshakeInterceptor[] interceptors = getInterceptors();
    		if (interceptors.length > 0) {
    			this.sockJsServiceRegistration.setInterceptors(interceptors);
    		}
    		if (this.handshakeHandler != null) {
    			WebSocketTransportHandler transportHandler = new WebSocketTransportHandler(this.handshakeHandler);
    			this.sockJsServiceRegistration.setTransportHandlerOverrides(transportHandler);
    		}
    		if (!this.allowedOrigins.isEmpty()) {
    			this.sockJsServiceRegistration.setAllowedOrigins(StringUtils.toStringArray(this.allowedOrigins));
    		}
    		if (!this.allowedOriginPatterns.isEmpty()) {
    			this.sockJsServiceRegistration.setAllowedOriginPatterns(
    					StringUtils.toStringArray(this.allowedOriginPatterns));
    		}
    		return this.sockJsServiceRegistration;
    	}
}
