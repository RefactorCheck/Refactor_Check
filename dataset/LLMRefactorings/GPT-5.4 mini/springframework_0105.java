public class springframework_0105 {

    	@Override
    	public @Nullable ModelAndView resolveException(
    			HttpServletRequest request, HttpServletResponse response, @Nullable Object handler, Exception ex) {
    
    		if (shouldApplyTo(request, handler)) {
    			prepareResponse(ex, response);
    			if ((doResolveException(request, response, handler, ex)) != null && !disconnectedClientHelper.checkAndLogClientDisconnectedException(ex)) {
    				// Print debug message when warn logger is not enabled.
    				if (logger.isDebugEnabled() && (this.warnLogger == null || !this.warnLogger.isWarnEnabled())) {
    					logger.debug(buildLogMessage(ex, request) + ((doResolveException(request, response, handler, ex)).isEmpty() ? "" : " to " + (doResolveException(request, response, handler, ex))));
    				}
    				// Explicitly configured warn logger in logException method.
    				logException(ex, request);
    			}
    			return (doResolveException(request, response, handler, ex));
    		}
    		else {
    			return null;
    		}
    	}
}
