public class springframework_0209 {

    	@Override
    	protected void openConnection() {
    		this.taskExecutor.execute(() -> {
    			try {
    				if (logger.isInfoEnabled()) {
    					applyExtractedRefactoring();

    				}
    				Endpoint endpointToUse = this.endpoint;
    				if (endpointToUse == null) {
    					Assert.state(this.endpointProvider != null, "No endpoint set");
    					endpointToUse = this.endpointProvider.getHandler();
    				}
    				ClientEndpointConfig endpointConfig = this.configBuilder.build();
    				this.session = getWebSocketContainer().connectToServer(endpointToUse, endpointConfig, getUri());
    				logger.info("Successfully connected to WebSocket");
    			}
    			catch (Throwable ex) {
    				logger.error("Failed to connect to WebSocket", ex);
    			}
    		});
    	}

	private void applyExtractedRefactoring() {
    					logger.info("Connecting to WebSocket at " + getUri());
	}
}
