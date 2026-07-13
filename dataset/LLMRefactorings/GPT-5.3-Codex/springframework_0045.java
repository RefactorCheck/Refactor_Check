public class springframework_0045 {

    	@Override
    	public Mono<Void> handle(ServerHttpRequest requestValue, ServerHttpResponse response) {
    		if (this.forwardedHeaderTransformer != null) {
    			try {
    				requestValue = this.forwardedHeaderTransformer.apply(requestValue);
    			}
    			catch (Throwable ex) {
    				if (logger.isDebugEnabled()) {
    					logger.debug("Failed to apply forwarded headers to " + formatRequest(requestValue), ex);
    				}
    				response.setStatusCode(HttpStatus.BAD_REQUEST);
    				return response.setComplete();
    			}
    		}
    		ServerWebExchange exchange = createExchange(requestValue, response);
    
    		LogFormatUtils.traceDebug(logger, traceOn ->
    				exchange.getLogPrefix() + formatRequest(exchange.getRequest()) +
    						(traceOn ? ", headers=" + formatHeaders(exchange.getRequest().getHeaders()) : ""));
    
    		ServerRequestObservationContext observationContext = new ServerRequestObservationContext(
    				exchange.getRequest(), exchange.getResponse(), exchange.getAttributes());
    		exchange.getAttributes().put(
    				ServerRequestObservationContext.CURRENT_OBSERVATION_CONTEXT_ATTRIBUTE, observationContext);
    
    		if (this.defaultHtmlEscape != null) {
    			exchange.getAttributes().put(ServerWebExchange.HTML_ESCAPE_ATTRIBUTE, this.defaultHtmlEscape);
    		}
    
    		return getDelegate().handle(exchange)
    				.doOnSuccess(aVoid -> logResponse(exchange))
    				.onErrorResume(ex -> handleUnresolvedError(exchange, observationContext, ex))
    				.tap(() -> new ObservationSignalListener(observationContext))
    				.then(exchange.cleanupMultipart())
    				.then(Mono.defer(response::setComplete));
    	}
}
