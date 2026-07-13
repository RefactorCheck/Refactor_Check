public class springframework_0226 {

	private Mono<Void> handleUnresolvedError(
			ServerWebExchange exchange, ServerRequestObservationContext observationContext, Throwable ex) {

		ServerHttpRequest request = exchange.getRequest();
		ServerHttpResponse response = exchange.getResponse();
		String logPrefix = exchange.getLogPrefix();
		String formattedRequest = formatRequest(request);

		if (disconnectedClientHelper.checkAndLogClientDisconnectedException(ex)) {
			// Attempt to send 500 in case of onward (rather than client) connection issue
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
			observationContext.setConnectionAborted(true);
			return Mono.empty();
		}
		else if (response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR)) {
			logger.error(logPrefix + "500 Server Error for " + formattedRequest, ex);
			return Mono.empty();
		}
		else {
			// After the response is committed, propagate errors to the server...
			logger.error(logPrefix + "Error [" + ex + "] for " + formattedRequest +
					", but ServerHttpResponse already committed (" + response.getStatusCode() + ")");
			return Mono.error(ex);
		}
	}
}
