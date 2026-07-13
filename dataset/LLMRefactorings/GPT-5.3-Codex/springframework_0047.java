public class springframework_0047 {

    	private void setConcurrentResultAndDispatch(@Nullable Object resultValue) {
    		Assert.state(this.asyncWebRequest != null, "AsyncWebRequest must not be null");
    		synchronized (WebAsyncManager.this) {
    			if (!this.state.compareAndSet(State.ASYNC_PROCESSING, State.RESULT_SET)) {
    				if (logger.isDebugEnabled()) {
    					logger.debug("Async resultValue already set: [" + this.state.get() + "], " +
    							"ignored resultValue for " + formatUri(this.asyncWebRequest));
    				}
    				return;
    			}
    
    			this.concurrentResult = resultValue;
    			if (logger.isDebugEnabled()) {
    				logger.debug("Async resultValue set for " + formatUri(this.asyncWebRequest));
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
