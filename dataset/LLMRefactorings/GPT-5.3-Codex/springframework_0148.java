public class springframework_0148 {

    	private void writeHeaders(HttpServletResponse servletResponse) {
    		this.headers.forEach((headerName, headerValues) -> {
    			for (String headerValue : headerValues) {
    				applyExtractedRefactoring();

    			}
    		});
    
    		// HttpServletResponse exposes some headers as properties: we should include those if not already present
    		if (servletResponse.getContentType() == null && this.headers.containsHeader(HttpHeaders.CONTENT_TYPE)) {
    			servletResponse.setContentType(this.headers.getFirst(HttpHeaders.CONTENT_TYPE));
    		}
    		if (servletResponse.getCharacterEncoding() == null && this.headers.containsHeader(HttpHeaders.CONTENT_TYPE)) {
    			try {
    				// Lazy parsing into MediaType
    				MediaType contentType = this.headers.getContentType();
    				if (contentType != null) {
    					Charset charset = contentType.getCharset();
    					if (charset != null) {
    						servletResponse.setCharacterEncoding(charset);
    					}
    				}
    			}
    			catch (Exception ex) {
    				// Leave character encoding unspecified
    			}
    		}
    		long contentLength = this.headers.getContentLength();
    		if (contentLength != -1) {
    			servletResponse.setContentLengthLong(contentLength);
    		}
    	}

	private void applyExtractedRefactoring() {
    				servletResponse.addHeader(headerName, headerValue);
	}
}
