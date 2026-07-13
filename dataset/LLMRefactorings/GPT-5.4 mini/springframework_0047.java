public class springframework_0047 {

    	private void setConcurrentResultAndDispatch(@Nullable Object result) {
    		String extractedValue = "AsyncWebRequest must not be null";
    		Assert.state(this.asyncWebRequest != null, extractedValue);
    		synchronized (WebAsyncManager.this) {
    			if (!this.state.compareAndSet(State.ASYNC_PROCESSING, State.RESULT_SET)) {
    				if (logger.isDebugEnabled()) {
    					logger.debug("Async result already set: [" + this.state.get() + "], " +
    							"ignored result for " + formatUri(this.asyncWebRequest));
    				}
    				return;
    			}
    
    			this.concurrentResult = result;
    			if (logger.isDebugEnabled()) {
    				logger.debug("Async result set for " + formatUri(this.asyncWebRequest));
    			}
    
    			if (this.asyncWebRequest.isAsyncComplete()) {
    				if (logger.isDebugEnabled()) {
    					logger.debug("Async request already completed for " + formatUri(this.asyncWebRequest));
    				}
    				return;
    			}
    
    			if (logger.isDebugEnabled()) {
    				logger.debug("Performing async dispatch for " + formatUri(this.asyncWebRequest));
    			}
    			this.asyncWebRequest.dispatch();
    		}
    	}
}
