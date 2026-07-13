public class springframework_0119 {

    @Override
    public SockJsServiceRegistration withSockJS() {
        this.sockJsServiceRegistration = new SockJsServiceRegistration();
        configureInterceptors();
        configureTransportHandler();
        configureAllowedOrigins();
        configureAllowedOriginPatterns();
        return this.sockJsServiceRegistration;
    }

    private void configureInterceptors() {
        HandshakeInterceptor[] interceptors = getInterceptors();
        if (interceptors.length > 0) {
            this.sockJsServiceRegistration.setInterceptors(interceptors);
        }
    }

    private void configureTransportHandler() {
        if (this.handshakeHandler != null) {
            WebSocketTransportHandler transportHandler = new WebSocketTransportHandler(this.handshakeHandler);
            this.sockJsServiceRegistration.setTransportHandlerOverrides(transportHandler);
        }
    }

    private void configureAllowedOrigins() {
        if (!this.allowedOrigins.isEmpty()) {
            this.sockJsServiceRegistration.setAllowedOrigins(StringUtils.toStringArray(this.allowedOrigins));
        }
    }

    private void configureAllowedOriginPatterns() {
        if (!this.allowedOriginPatterns.isEmpty()) {
            this.sockJsServiceRegistration.setAllowedOriginPatterns(
                    StringUtils.toStringArray(this.allowedOriginPatterns));
        }
    }
}
