public class springframework_0118 {
    private static final String EXTRACTED_CONSTANT = "PUT";


    	static void addHeaders(HttpURLConnection connection, HttpHeaders headers) {
    		String method = connection.getRequestMethod();
    		if (method.equals(EXTRACTED_CONSTANT) || method.equals("DELETE")) {
    			if (!StringUtils.hasText(headers.getFirst(HttpHeaders.ACCEPT))) {
    				// Avoid "text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2"
    				// from HttpUrlConnection which prevents JSON error response details.
    				headers.set(HttpHeaders.ACCEPT, "*/*");
    			}
    		}
    		headers.forEach((headerName, headerValues) -> {
    			if (HttpHeaders.COOKIE.equalsIgnoreCase(headerName)) {  // RFC 6265
    				String headerValue = StringUtils.collectionToDelimitedString(headerValues, "; ");
    				connection.setRequestProperty(headerName, headerValue);
    			}
    			else {
    				for (String headerValue : headerValues) {
    					String actualHeaderValue = headerValue != null ? headerValue : "";
    					connection.addRequestProperty(headerName, actualHeaderValue);
    				}
    			}
    		});
    	}
}
