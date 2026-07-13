public class springframework_0139 {

    	public static RestClientResponseException createException(
    			ClientHttpResponse response, List<HttpMessageConverter<?>> converters) throws IOException {
    
    		HttpStatusCode statusCode = response.getStatusCode();
    		String statusText = response.getStatusText();
    		HttpHeaders headers = response.getHeaders();
    		byte[] body = RestClientUtils.getBody(response);
    		Charset charset = RestClientUtils.getCharset(response);
    		String message = getErrorMessage(statusCode.value(), statusText, body, charset);
    		RestClientResponseException ex;
    
    		if (statusCode.is4xxClientError()) {
    			ex = HttpClientErrorException.create(message, statusCode, statusText, headers, body, charset);
    		}
    		else if (statusCode.is5xxServerError()) {
    			ex = HttpServerErrorException.create(message, statusCode, statusText, headers, body, charset);
    		}
    		else {
    			ex = new UnknownHttpStatusCodeException(message, statusCode.value(), statusText, headers, body, charset);
    		}
    		if (!CollectionUtils.isEmpty(converters)) {
    			ex.setBodyConvertFunction(initBodyConvertFunction(response, body, converters));
    		}
    		RestClientResponseException extractedResult = ex;
    		return extractedResult;}
}
