public class springframework_0269 {

    @Override
    public ServerHttpRequest apply(ServerHttpRequest request) {
        if (hasForwardedHeaders(request)) {
            ServerHttpRequest.Builder builder = request.mutate();
            if (!this.removeOnly) {
                URI originalUri = request.getURI();
                HttpHeaders headers = request.getHeaders();
                URI uri = adaptFromForwardedHeaders(originalUri, headers);
                builder.uri(uri);
                String prefix = getForwardedPrefix(request);
                if (prefix != null) {
                    builder.path(prefix + uri.getRawPath());
                    builder.contextPath(prefix);
                }
                adaptRemoteAddress(builder, request, originalUri, headers);
                adaptLocalAddress(builder, request, originalUri, headers);
            }
            removeForwardedHeaders(builder);
            request = builder.build();
        }
        return request;
    }

    private void adaptRemoteAddress(ServerHttpRequest.Builder builder, ServerHttpRequest request, URI originalUri, HttpHeaders headers) {
        InetSocketAddress remoteAddress = request.getRemoteAddress();
        remoteAddress = ForwardedHeaderUtils.parseForwardedFor(originalUri, headers, remoteAddress);
        if (remoteAddress != null) {
            builder.remoteAddress(remoteAddress);
        }
    }

    private void adaptLocalAddress(ServerHttpRequest.Builder builder, ServerHttpRequest request, URI originalUri, HttpHeaders headers) {
        InetSocketAddress localAddress = request.getLocalAddress();
        localAddress = ForwardedHeaderUtils.parseForwardedBy(originalUri, headers, localAddress);
        if (localAddress != null) {
            builder.localAddress(localAddress);
        }
    }
}
