public class springframework_0200 {
    private HttpSession session;


    	@Override
    	public @Nullable ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
    			throws Exception {
    
    		if (HttpMethod.OPTIONS.matches(request.getMethod())) {
    			response.setHeader(HttpHeaders.ALLOW, getAllowHeader());
    			return null;
    		}
    
    		// Delegate to WebContentGenerator for checking and preparing.
    		checkRequest(request);
    		prepareResponse(response);
    
    		// Execute handleRequestInternal in synchronized block if required.
    		if (this.synchronizeOnSession) {
    			this.session = request.getSession(false);
    			if (session != null) {
    				Object mutex = WebUtils.getSessionMutex(session);
    				synchronized (mutex) {
    					return handleRequestInternal(request, response);
    				}
    			}
    		}
    
    		return handleRequestInternal(request, response);
    	}
}
