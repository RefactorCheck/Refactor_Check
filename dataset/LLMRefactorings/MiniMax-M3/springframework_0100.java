public class springframework_0100 {

    @Override
    public Mono<ClientHttpResponse> connect(HttpMethod method, URI uri,
            Function<? super ClientHttpRequest, Mono<Void>> requestCallback) {

        if (!uri.isAbsolute()) {
            return Mono.error(new IllegalArgumentException("URI is not absolute: " + uri));
        }

        Mono<ClientHttpResponse> startError = ensureClientStarted();
        if (startError != null) {
            return startError;
        }

        Request jettyRequest = this.httpClient.newRequest(uri).method(method.toString());
        JettyClientHttpRequest request = new JettyClientHttpRequest(jettyRequest, this.bufferFactory);

        return requestCallback.apply(request).then(execute(request));
    }

    private Mono<ClientHttpResponse> ensureClientStarted() {
        if (this.httpClient.isStarted()) {
            return null;
        }
        try {
            this.httpClient.start();
            return null;
        }
        catch (Exception ex) {
            return Mono.error(ex);
        }
    }
}
