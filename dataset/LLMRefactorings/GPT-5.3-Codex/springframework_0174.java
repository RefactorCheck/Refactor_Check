public class springframework_0174 {

    	@Override
    	@SuppressWarnings("try")
    	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
    			throws ServletException, IOException {
    
    		applyExtractedRefactoring();

    		try (Observation.Scope scope = observation.openScope()) {
    			onScopeOpened(scope, request, response);
    			filterChain.doFilter(request, response);
    		}
    		catch (Exception ex) {
    			observation.error(unwrapServletException(ex));
    			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
    			throw ex;
    		}
    		finally {
    			// If async is started during the first dispatch, register a listener for completion notification.
    			if (request.isAsyncStarted() && request.getDispatcherType() == DispatcherType.REQUEST) {
    				request.getAsyncContext().addListener(new ObservationAsyncListener(observation));
    			}
    			// scope is opened for ASYNC dispatches, but the observation will be closed
    			// by the async listener.
    			else if (!isAsyncDispatch(request)) {
    				Throwable error = fetchException(request);
    				if (error != null) {
    					observation.error(error);
    				}
    				observation.stop();
    			}
    		}
    	}

	private void applyExtractedRefactoring() {
    		Observation observation = createOrFetchObservation(request, response);
	}
}
