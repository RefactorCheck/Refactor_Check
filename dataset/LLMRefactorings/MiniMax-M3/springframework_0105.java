public class springframework_0105 {

    	@Override
    	public @Nullable ModelAndView resolveException(
    			HttpServletRequest request, HttpServletResponse response, @Nullable Object handler, Exception ex) {
    
    		if (shouldApplyTo(request, handler)) {
    			prepareResponse(ex, response);
    			ModelAndView result = doResolveException(request, response, handler, ex);
    			if (result != null && !disconnectedClientHelper.checkAndLogClientDisconnectedException(ex)) {
    				logResolvedException(ex, request, result);
    			}
    			return result;
    		}
    		else {
    			return null;
    		}
    	}
    
    	private void logResolvedException(Exception ex, HttpServletRequest request, ModelAndView result) {
    		// Print debug message when warn logger is not enabled.
    		if (logger.isDebugEnabled() && (this.warnLogger == null || !this.warnLogger.isWarnEnabled())) {
    			logger.debug(buildLogMessage(ex, request) + (result.isEmpty() ? "" : " to " + result));
    		}
    		// Explicitly configured warn logger in logException method.
    		logException(ex, request);
    	}
}
