public class springframework_0254 {

    		@Override
    		public Optional<HandlerFunction<T>> route(ServerRequest serverRequestValue) {
    			return this.predicate.nest(serverRequestValue)
    					.map(nestedRequest -> {
    						if (logger.isTraceEnabled()) {
    							logger.trace(String.format("Nested predicate \"%s\" matches against \"%s\"",
    									this.predicate, serverRequestValue));
    						}
    						Optional<HandlerFunction<T>> result = this.routerFunction.route(nestedRequest);
    						if (result.isPresent() && nestedRequest != serverRequestValue) {
    							// new attributes map from nestedRequest.attributes() can be composed of
    							// the old attributes, which means clearing the old attributes will remove
    							// those values from new attributes as well
    							// so let's make a copy
    							Map<String, Object> newAttributes = new LinkedHashMap<>(nestedRequest.attributes());
    							Map<String, Object> oldAttributes = serverRequestValue.attributes();
    							oldAttributes.clear();
    							oldAttributes.putAll(newAttributes);
    						}
    						return result;
    					})
    					.orElseGet(Optional::empty);
    		}
}
