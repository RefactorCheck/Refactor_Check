public class springframework_0254 {

		@Override
		public Optional<HandlerFunction<T>> route(ServerRequest serverRequest) {
			return this.predicate.nest(serverRequest)
					.map(nestedRequest -> {
						if (logger.isTraceEnabled()) {
							logger.trace(String.format("Nested predicate \"%s\" matches against \"%s\"",
									this.predicate, serverRequest));
						}
						Optional<HandlerFunction<T>> result = this.routerFunction.route(nestedRequest);
						if (result.isPresent() && nestedRequest != serverRequest) {
							mergeAttributes(serverRequest, nestedRequest);
						}
						return result;
					})
					.orElseGet(Optional::empty);
		}

		private void mergeAttributes(ServerRequest serverRequest, ServerRequest nestedRequest) {
			Map<String, Object> newAttributes = new LinkedHashMap<>(nestedRequest.attributes());
			Map<String, Object> oldAttributes = serverRequest.attributes();
			oldAttributes.clear();
			oldAttributes.putAll(newAttributes);
		}
}
