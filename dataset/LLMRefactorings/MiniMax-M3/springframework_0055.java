public class springframework_0055 {

    	@Override
    	public void afterPropertiesSet() {
    		Assert.state(this.servletContext != null,
    				"A ServletContext is required to access the jakarta.websocket.server.ServerContainer instance");
    		this.serverContainer = (ServerContainer) this.servletContext.getAttribute(
    				"jakarta.websocket.server.ServerContainer");
    		Assert.state(this.serverContainer != null,
    				"Attribute 'jakarta.websocket.server.ServerContainer' not found in ServletContext");

    		configureServerContainer();
    	}

    	private void configureServerContainer() {
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
