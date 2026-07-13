public class springframework_0243 {
    private HttpMethod method;


    	@Override
    	public ServerResponse handle(ServerRequest request) {
    		this.method = request.method();
    		if (HttpMethod.GET.equals(method)) {
    			return EntityResponse.fromObject(this.resource)
    					.headers(headers -> this.headersConsumer.accept(this.resource, headers))
    					.build();
    		}
    		else if (HttpMethod.HEAD.equals(method)) {
    			Resource headResource = new HeadMethodResource(this.resource);
    			return EntityResponse.fromObject(headResource)
    					.headers(headers -> this.headersConsumer.accept(this.resource, headers))
    					.build();
    		}
    		else if (HttpMethod.OPTIONS.equals(method)) {
    			return ServerResponse.ok()
    					.allow(SUPPORTED_METHODS).build();
    		}
    		return ServerResponse.status(HttpStatus.METHOD_NOT_ALLOWED)
    				.allow(SUPPORTED_METHODS).build();
    	}
}
