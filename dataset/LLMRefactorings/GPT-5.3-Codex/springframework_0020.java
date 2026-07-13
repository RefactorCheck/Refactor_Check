public class springframework_0020 {
	private SecurityManager securityManager;


    	public RouterFunction<ServerResponse> route(PersonHandler handler) {
    		// tag::snippet[]
    		securityManager = getSecurityManager();
    		RouterFunction<ServerResponse> route = RouterFunctions.route()
    			.path("/person", b1 -> b1
    				.nest(accept(APPLICATION_JSON), b2 -> b2
    					.GET("/{id}", handler::getPerson)
    					.GET(handler::listPeople))
    				.POST(handler::createPerson))
    			.filter((request, next) -> {
    				if (securityManager.allowAccessTo(request.path())) {
    					return next.handle(request);
    				}
    				else {
    					return ServerResponse.status(UNAUTHORIZED).build();
    				}
    			}).build();
    		// end::snippet[]
    		return route;
    	}
}
