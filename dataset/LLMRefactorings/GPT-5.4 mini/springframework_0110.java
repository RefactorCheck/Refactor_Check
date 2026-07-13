public class springframework_0110 {

    		public static void onError(Throwable ex) {
    			if (logger.isTraceEnabled()) {
    				logger.trace(this.logPrefix + "onError: " + ex);
    			}
    			runIfAsyncNotComplete(this.asyncContext, this.completionFlag, () -> {
    				if (this.asyncContext.getResponse().isCommitted()) {
    					logger.trace(this.logPrefix + "Dispatch to container, to raise the error on servlet thread");
    					this.asyncContext.getRequest().setAttribute(WRITE_ERROR_ATTRIBUTE_NAME, ex);
    					this.asyncContext.dispatch();
    				}
    				else {
    					try {
    						logger.trace(this.logPrefix + "Setting ServletResponse status to 500 Server Error");
    						this.asyncContext.getResponse().resetBuffer();
    						((HttpServletResponse) this.asyncContext.getResponse()).setStatus(500);
    					}
    					finally {
    						this.asyncContext.complete();
    					}
    				}
    			});
    		}
}
