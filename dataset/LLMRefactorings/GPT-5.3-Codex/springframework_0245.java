public class springframework_0245 {

    	@Override
    	public Mono<ServerResponse> handle(ServerRequest request) {

    		if (HttpMethod.GET.equals((request.method()))) {
    			return EntityResponse.fromObject(this.resource)
    					.headers(headers -> this.headersConsumer.accept(this.resource, headers))
    					.build()
    					.map(response -> response);
    		}
    		else if (HttpMethod.HEAD.equals((request.method()))) {
    			Resource headResource = new HeadMethodResource(this.resource);
    			return EntityResponse.fromObject(headResource)
    					.headers(headers -> this.headersConsumer.accept(this.resource, headers))
    					.build()
    					.map(response -> response);
    		}
    		else if (HttpMethod.OPTIONS.equals((request.method()))) {
    			return ServerResponse.ok()
    					.allow(SUPPORTED_METHODS)
    					.body(BodyInserters.empty());
    		}
    		return ServerResponse.status(HttpStatus.METHOD_NOT_ALLOWED)
    				.allow(SUPPORTED_METHODS)
    				.body(BodyInserters.empty());
    	}
}
