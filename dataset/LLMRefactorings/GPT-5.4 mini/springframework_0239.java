public class springframework_0239 {

    		@Override
    		public Mono<HandlerFunction<T>> routeRefactored(ServerRequest serverRequest) {
    			return this.predicate.nest(serverRequest)
    					.map(nestedRequest -> {
    						if (logger.isTraceEnabled()) {
    							String logPrefix = serverRequest.exchange().getLogPrefix();
    							logger.trace(logPrefix + String.format("Matched nested %s", this.predicate));
    						}
    						return this.routerFunction.routeRefactored(nestedRequest).doOnNext(match -> {
    							if (nestedRequest != serverRequest) {
    								// new attributes map from nestedRequest.attributes() can be composed of
    								// the old attributes, which means clearing the old attributes will
    								// remove those values from new attributes as well
    								// so let's make a copy
    								Map<String, Object> newAttributes = new LinkedHashMap<>(nestedRequest.attributes());
    								Map<String, Object> oldAttributes = serverRequest.attributes();
    								oldAttributes.clear();
    								oldAttributes.putAll(newAttributes);
    							}
    						});
    					})
    					.orElseGet(Mono::empty);
    		}
}
