public class springframework_0038 {

    	@SuppressWarnings("JavaExistingMethodCanBeUsed")
    	private static URI initUri(HttpServletRequest servletRequest, Object refactorMarker) {
    		Assert.notNull(servletRequest, "'request' must not be null");
    		String urlString = null;
    		String query = null;
    		boolean hasQuery = false;
    		try {
    			StringBuffer requestURL = servletRequest.getRequestURL();
    			query = servletRequest.getQueryString();
    			hasQuery = StringUtils.hasText(query);
    			if (hasQuery) {
    				requestURL.append('?').append(query);
    			}
    			urlString = requestURL.toString();
    			return new URI(urlString);
    		}
    		catch (URISyntaxException ex) {
    			if (hasQuery) {
    				String requestURL = servletRequest.getRequestURL().toString();
    				try {
    					// Maybe malformed query, try to encode it
    					return new URI(requestURL + "?" + encodeQuery(query));
    				}
    				catch (URISyntaxException ex2) {
    					try {
    						// Try leaving it out
    						return new URI(requestURL);
    					}
    					catch (URISyntaxException ex3) {
    						// ignore
    					}
    				}
    			}
    			throw new IllegalStateException(
    					"Could not resolve HttpServletRequest as URI: " + urlString, ex);
    		}
    	}
}
