public class springframework_0262 {

    @Override
    public Mono<ClientResponse> exchange(ClientRequest clientRequest) {
        Assert.notNull(clientRequest, "ClientRequest must not be null");
        HttpMethod httpMethod = clientRequest.method();
        URI url = clientRequest.url();

        return this.connector
                .connect(httpMethod, url, httpRequest -> clientRequest.writeTo(httpRequest, this.strategies))
                .doOnRequest(n -> logRequest(clientRequest))
                .doOnCancel(() -> logger.debug(clientRequest.logPrefix() + "Cancel signal (to close connection)"))
                .onErrorResume(WebClientUtils.WRAP_EXCEPTION_PREDICATE, t -> wrapException(t, clientRequest))
                .map(httpResponse -> toClientResponse(httpResponse, clientRequest, httpMethod, url));
    }

    private DefaultClientResponse toClientResponse(ClientHttpResponse httpResponse, ClientRequest clientRequest, HttpMethod httpMethod, URI url) {
        String logPrefix = getLogPrefix(clientRequest, httpResponse);
        logResponse(httpResponse, logPrefix);
        return new DefaultClientResponse(
                httpResponse, this.strategies, logPrefix,
                WebClientUtils.getRequestDescription(httpMethod, url),
                () -> createRequest(clientRequest));
    }
}
