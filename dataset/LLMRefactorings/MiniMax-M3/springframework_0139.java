public class springframework_0139 {

	public static RestClientResponseException createException(
			ClientHttpResponse response, List<HttpMessageConverter<?>> converters) throws IOException {

		HttpStatusCode statusCode = response.getStatusCode();
		String statusText = response.getStatusText();
		HttpHeaders headers = response.getHeaders();
		byte[] body = RestClientUtils.getBody(response);
		Charset charset = RestClientUtils.getCharset(response);
		String message = getErrorMessage(statusCode.value(), statusText, body, charset);
		RestClientResponseException ex = createExceptionForStatus(statusCode, statusText, headers, body, charset, message);

		if (!CollectionUtils.isEmpty(converters)) {
			ex.setBodyConvertFunction(initBodyConvertFunction(response, body, converters));
		}
		return ex;
	}

	private static RestClientResponseException createExceptionForStatus(
			HttpStatusCode statusCode, String statusText, HttpHeaders headers,
			byte[] body, Charset charset, String message) {

		if (statusCode.is4xxClientError()) {
			return HttpClientErrorException.create(message, statusCode, statusText, headers, body, charset);
		}
		else if (statusCode.is5xxServerError()) {
			return HttpServerErrorException.create(message, statusCode, statusText, headers, body, charset);
		}
		else {
			return new UnknownHttpStatusCodeException(message, statusCode.value(), statusText, headers, body, charset);
		}
	}
}
