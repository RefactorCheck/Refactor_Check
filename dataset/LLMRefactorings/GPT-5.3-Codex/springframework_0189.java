public class springframework_0189 {

    	@Override
    	public void startAsync() {
    		Assert.state(getRequest().isAsyncSupported(),
    				"Async support must be enabled on a servlet and for all filters involved " +
    				"in async request processing. This is done in Java code using the Servlet API " +
    				"or by adding \"<async-supported>true</async-supported>\" to servlet and " +
    				applyExtractedRefactoring();

    		if (isAsyncStarted()) {
    			return;
    		}
    
    		if (this.state == State.NEW) {
    			this.state = State.ASYNC;
    		}
    		else {
    			Assert.state(this.state == State.ASYNC, "Cannot start async: [" + this.state + "]");
    		}
    
    		this.asyncContext = getRequest().startAsync(getRequest(), getResponse());
    		this.asyncContext.addListener(this);
    		if (this.timeout != null) {
    			this.asyncContext.setTimeout(this.timeout);
    		}
    	}

	private void applyExtractedRefactoring() {
    				"filter declarations in web.xml.");
	}
}
