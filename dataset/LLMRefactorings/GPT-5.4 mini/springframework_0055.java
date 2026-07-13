public class springframework_0055 {
    private static final String EXTRACTED_CONSTANT = "A ServletContext is required to access the jakarta.websocket.server.ServerContainer instance";


    	@Override
    	public void afterPropertiesSet() {
    		Assert.state(this.servletContext != null,
    				EXTRACTED_CONSTANT);
    		this.serverContainer = (ServerContainer) this.servletContext.getAttribute(
    				"jakarta.websocket.server.ServerContainer");
    		Assert.state(this.serverContainer != null,
    				"Attribute 'jakarta.websocket.server.ServerContainer' not found in ServletContext");
    
    		if (this.asyncSendTimeout != null) {
    			this.serverContainer.setAsyncSendTimeout(this.asyncSendTimeout);
    		}
    		if (this.maxSessionIdleTimeout != null) {
    			this.serverContainer.setDefaultMaxSessionIdleTimeout(this.maxSessionIdleTimeout);
    		}
    		if (this.maxTextMessageBufferSize != null) {
    			this.serverContainer.setDefaultMaxTextMessageBufferSize(this.maxTextMessageBufferSize);
    		}
    		if (this.maxBinaryMessageBufferSize != null) {
    			this.serverContainer.setDefaultMaxBinaryMessageBufferSize(this.maxBinaryMessageBufferSize);
    		}
    	}
}
