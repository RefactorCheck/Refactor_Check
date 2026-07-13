public class springframework_0239 {

    @Override
    public Mono<HandlerFunction<T>> route(ServerRequest serverRequest) {
        return this.predicate.nest(serverRequest)
                .map(nestedRequest -> routeNested(serverRequest, nestedRequest))
                .orElseGet(Mono::empty);
    }

    private Mono<HandlerFunction<T>> routeNested(ServerRequest serverRequest, ServerRequest nestedRequest) {
        if (logger.isTraceEnabled()) {
            String logPrefix = serverRequest.exchange().getLogPrefix();
            logger.trace(logPrefix + String.format("Matched nested %s", this.predicate));
        }
        return this.routerFunction.route(nestedRequest).doOnNext(match -> {
            if (nestedRequest != serverRequest) {
                Map<String, Object> newAttributes = new LinkedHashMap<>(nestedRequest.attributes());
                Map<String, Object> oldAttributes = serverRequest.attributes();
                oldAttributes.clear();
                oldAttributes.putAll(newAttributes);
            }
        });
    }
}
