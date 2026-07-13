public class springframework_0245 {

	@Override
	public Mono<ServerResponse> handle(ServerRequest request) {
		HttpMethod method = request.method();
		if (HttpMethod.GET.equals(method)) {
			return buildEntityResponse(this.resource);
		}
		else if (HttpMethod.HEAD.equals(method)) {
			return buildEntityResponse(new HeadMethodResource(this.resource));
		}
		else if (HttpMethod.OPTIONS.equals(method)) {
			return ServerResponse.ok()
					.allow(SUPPORTED_METHODS)
					.body(BodyInserters.empty());
		}
		return ServerResponse.status(HttpStatus.METHOD_NOT_ALLOWED)
				.allow(SUPPORTED_METHODS)
				.body(BodyInserters.empty());
	}

	private Mono<EntityResponse<Resource>> buildEntityResponse(Resource resource) {
		return EntityResponse.fromObject(resource)
				.headers(headers -> this.headersConsumer.accept(this.resource, headers))
				.build()
				.map(response -> response);
	}
}
