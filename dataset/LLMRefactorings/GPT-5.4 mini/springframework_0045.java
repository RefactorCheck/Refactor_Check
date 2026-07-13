public class springframework_0045 {

    	@Override
    	public Mono<Void> handle(ServerHttpRequest request, ServerHttpResponse response) {
    		if (this.forwardedHeaderTransformer != null) {
    			try {
    				request = this.forwardedHeaderTransformer.apply(request);
    			}
    			catch (Throwable ex) {
    				if (logger.isDebugEnabled()) {
    					logger.debug("Failed to apply forwarded headers to " + formatRequest(request), ex);
    				}
    				response.setStatusCode(HttpStatus.BAD_REQUEST);
    				return response.setComplete();
    			}
    		}
    
    		LogFormatUtils.traceDebug(logger, traceOn ->
    				(createExchange(request, response)).getLogPrefix() + formatRequest((createExchange(request, response)).getRequest()) +
    						(traceOn ? ", headers=" + formatHeaders((createExchange(request, response)).getRequest().getHeaders()) : ""));
    
    		ServerRequestObservationContext observationContext = new ServerRequestObservationContext(
    				(createExchange(request, response)).getRequest(), (createExchange(request, response)).getResponse(), (createExchange(request, response)).getAttributes());
    		(createExchange(request, response)).getAttributes().put(
    				ServerRequestObservationContext.CURRENT_OBSERVATION_CONTEXT_ATTRIBUTE, observationContext);
    
    		if (this.defaultHtmlEscape != null) {
    			(createExchange(request, response)).getAttributes().put(ServerWebExchange.HTML_ESCAPE_ATTRIBUTE, this.defaultHtmlEscape);
    		}
    
    		return getDelegate().handle((createExchange(request, response)))
    				.doOnSuccess(aVoid -> logResponse((createExchange(request, response))))
    				.onErrorResume(ex -> handleUnresolvedError((createExchange(request, response)), observationContext, ex))
    				.tap(() -> new ObservationSignalListener(observationContext))
    				.then((createExchange(request, response)).cleanupMultipart())
    				.then(Mono.defer(response::setComplete));
    	}
}
