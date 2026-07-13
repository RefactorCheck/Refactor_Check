public class springframework_0047 {

    	private void setConcurrentResultAndDispatch(@Nullable Object result) {
    		Assert.state(this.asyncWebRequest != null, "AsyncWebRequest must not be null");
    		String uri = formatUri(this.asyncWebRequest);
    		synchronized (WebAsyncManager.this) {
    			if (!this.state.compareAndSet(State.ASYNC_PROCESSING, State.RESULT_SET)) {
    				if (logger.isDebugEnabled()) {
    					logger.debug("Async result already set: [" + this.state.get() + "], " +
    							"ignored result for " + uri);
    				}
    				return;
    			}
    
    			this.concurrentResult = result;
    			if (logger.isDebugEnabled()) {
    				logger.debug("Async result set for " + uri);
    			}
    
    			if (this.asyncWebRequest.isAsyncComplete()) {
    				if (logger.isDebugEnabled()) {
    					logger.debug("Async request already completed for " + uri);
    				}
    				return;
    			}
    
    			if (logger.isDebugEnabled()) {
    				logger.debug("Performing async dispatch for " + uri);
    			}
    			this.asyncWebRequest.dispatch();
    		}
    	}
}
