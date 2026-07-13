public class springframework_0231 {

    	@Override
    	public boolean process(@Nullable CorsConfiguration config, ServerWebExchange exchange) {
    		ServerHttpRequest request = exchange.getRequest();
    
    		if (config == null) {
    			if (logger.isDebugEnabled() && CorsUtils.isCorsRequest(request)) {
    				logger.debug("Skip: no CORS configuration has been provided");
    			}
    			return true;
    		}
    
    		ServerHttpResponse response = exchange.getResponse();
    		HttpHeaders responseHeaders = response.getHeaders();
    
    		List<String> varyHeaders = responseHeaders.get(HttpHeaders.VARY);
    		if (varyHeaders == null) {
    			responseHeaders.addAll(HttpHeaders.VARY, VARY_HEADERS);
    		}
    		else {
    			for (String header : VARY_HEADERS) {
    				if (!varyHeaders.contains(header)) {
    					responseHeaders.add(HttpHeaders.VARY, header);
    				}
    			}
    		}
    
    		try {
    			if (!CorsUtils.isCorsRequest(request)) {
    				return true;
    			}
    		}
    		catch (IllegalArgumentException ex) {
    			logger.debug("Reject: origin is malformed");
    			rejectRequest(response);
    			return false;
    		}
    
    		if (responseHeaders.getFirst(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN) != null) {
    			logger.trace("Skip: response already contains \"Access-Control-Allow-Origin\"");
    			return true;
    		}
    
    		return handleInternal(exchange, config, CorsUtils.isPreFlightRequest(request));
    	}
}
