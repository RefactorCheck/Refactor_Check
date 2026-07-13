public class springframework_0209 {

    	@Override
    	protected void openConnection() {
    		this.taskExecutor.execute(() -> {
    			try {
    				if (logger.isInfoEnabled()) {
    					logger.info("Connecting to WebSocket at " + getUri());
    				}
    				if ((this.endpoint) == null) {
    					Assert.state(this.endpointProvider != null, "No endpoint set");
    					(this.endpoint) = this.endpointProvider.getHandler();
    				}
    				ClientEndpointConfig endpointConfig = this.configBuilder.build();
    				this.session = getWebSocketContainer().connectToServer((this.endpoint), endpointConfig, getUri());
    				logger.info("Successfully connected to WebSocket");
    			}
    			catch (Throwable ex) {
    				logger.error("Failed to connect to WebSocket", ex);
    			}
    		});
    	}
}
