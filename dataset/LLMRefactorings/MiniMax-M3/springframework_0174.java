public class springframework_0174 {

    	@Override
    	@SuppressWarnings("try")
    	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
    			throws ServletException, IOException {
    
    		Observation observation = createOrFetchObservation(request, response);
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
    			handleObservationLifecycle(request, observation);
    		}
    	}

    	private void handleObservationLifecycle(HttpServletRequest request, Observation observation) {
    		if (request.isAsyncStarted() && request.getDispatcherType() == DispatcherType.REQUEST) {
    			request.getAsyncContext().addListener(new ObservationAsyncListener(observation));
    		}
    		else if (!isAsyncDispatch(request)) {
    			Throwable error = fetchException(request);
    			if (error != null) {
    				observation.error(error);
    			}
    			observation.stop();
    		}
    	}
}
