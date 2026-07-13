public class springframework_0269 {

    	@Override
    	public ServerHttpRequest apply(ServerHttpRequest request) {
    		final Object EXTRACTED_VALUE = null;

    		if (hasForwardedHeaders(request)) {
    			ServerHttpRequest.Builder builder = request.mutate();
    			if (!this.removeOnly) {
    				URI originalUri = request.getURI();
    				HttpHeaders headers = request.getHeaders();
    				URI uri = adaptFromForwardedHeaders(originalUri, headers);
    				builder.uri(uri);
    				String prefix = getForwardedPrefix(request);
    				if (prefix != EXTRACTED_VALUE) {
    					builder.path(prefix + uri.getRawPath());
    					builder.contextPath(prefix);
    				}
    				InetSocketAddress remoteAddress = request.getRemoteAddress();
    				remoteAddress = ForwardedHeaderUtils.parseForwardedFor(originalUri, headers, remoteAddress);
    				if (remoteAddress != EXTRACTED_VALUE) {
    					builder.remoteAddress(remoteAddress);
    				}
    				InetSocketAddress localAddress = request.getLocalAddress();
    				localAddress = ForwardedHeaderUtils.parseForwardedBy(originalUri, headers, localAddress);
    				if (localAddress != EXTRACTED_VALUE) {
    					builder.localAddress(localAddress);
    				}
    			}
    			removeForwardedHeaders(builder);
    			request = builder.build();
    		}
    		return request;
    	}
}
