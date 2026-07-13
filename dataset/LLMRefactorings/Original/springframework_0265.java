public class springframework_0265 {

    	@SuppressWarnings({"NullAway", "removal"})
    	private static Function<ResolvableType, ? extends @Nullable Object> initBodyConvertFunction(
    			ClientHttpResponse response, byte[] body, List<HttpMessageConverter<?>> messageConverters) {
    
    		return resolvableType -> {
    			try {
    				HttpMessageConverterExtractor<?> extractor =
    						new HttpMessageConverterExtractor<>(resolvableType.getType(), messageConverters);
    
    				return extractor.extractData(new ClientHttpResponseDecorator(response) {
    					@Override
    					public InputStream getBody() {
    						return new ByteArrayInputStream(body);
    					}
    				});
    			}
    			catch (IOException ex) {
    				throw new RestClientException("Error while extracting response for type [" + resolvableType + "]", ex);
    			}
    		};
    	}
}
