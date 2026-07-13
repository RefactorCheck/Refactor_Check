public class springframework_0127 {

    	@Override
    	public SockJsServiceRegistration withSockJS() {
    		applyExtractedRefactoring();

    		this.registration.setTaskScheduler(this.sockJsTaskScheduler);
    		HandshakeInterceptor[] interceptors = getInterceptors();
    		if (interceptors.length > 0) {
    			this.registration.setInterceptors(interceptors);
    		}
    		if (this.handshakeHandler != null) {
    			WebSocketTransportHandler handler = new WebSocketTransportHandler(this.handshakeHandler);
    			this.registration.setTransportHandlerOverrides(handler);
    		}
    		if (!this.allowedOrigins.isEmpty()) {
    			this.registration.setAllowedOrigins(StringUtils.toStringArray(this.allowedOrigins));
    		}
    		if (!this.allowedOriginPatterns.isEmpty()) {
    			this.registration.setAllowedOriginPatterns(StringUtils.toStringArray(this.allowedOriginPatterns));
    		}
    		return this.registration;
    	}

	private void applyExtractedRefactoring() {
    		this.registration = new SockJsServiceRegistration();
	}
}
