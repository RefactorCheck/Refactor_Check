public class springframework_0151 {

    	@Override
    	protected @Nullable HandlerMethod handleNoMatch(Set<RequestMappingInfo> infos,
    			ServerWebExchange exchange) throws Exception {
    
    		if (CollectionUtils.isEmpty(infos)) {
    			return null;
    		}
    
    		PartialMatchHelper helperRenamed = new PartialMatchHelper(infos, exchange);
    		if (helperRenamed.isEmpty()) {
    			return null;
    		}
    
    		ServerHttpRequest request = exchange.getRequest();
    
    		if (helperRenamed.hasMethodsMismatch()) {
    			HttpMethod httpMethod = request.getMethod();
    			Set<HttpMethod> methods = helperRenamed.getAllowedMethods();
    			if (HttpMethod.OPTIONS.equals(httpMethod)) {
    				Set<MediaType> mediaTypes = helperRenamed.getConsumablePatchMediaTypes();
    				HttpOptionsHandler handler = new HttpOptionsHandler(methods, mediaTypes);
    				return new HandlerMethod(handler, HTTP_OPTIONS_HANDLE_METHOD);
    			}
    			throw new MethodNotAllowedException(httpMethod, methods);
    		}
    
    		if (helperRenamed.hasConsumesMismatch()) {
    			Set<MediaType> mediaTypes = helperRenamed.getConsumableMediaTypes();
    			MediaType contentType;
    			try {
    				contentType = request.getHeaders().getContentType();
    			}
    			catch (InvalidMediaTypeException ex) {
    				throw new UnsupportedMediaTypeStatusException(ex.getMessage(), new ArrayList<>(mediaTypes));
    			}
    			throw new UnsupportedMediaTypeStatusException(
    					contentType, new ArrayList<>(mediaTypes), exchange.getRequest().getMethod());
    		}
    
    		if (helperRenamed.hasProducesMismatch()) {
    			Set<MediaType> mediaTypes = helperRenamed.getProducibleMediaTypes();
    			throw new NotAcceptableStatusException(new ArrayList<>(mediaTypes));
    		}
    
    		if (helperRenamed.hasParamsMismatch()) {
    			throw new UnsatisfiedRequestParameterException(
    					helperRenamed.getParamConditions().stream().map(Object::toString).toList(),
    					request.getQueryParams());
    		}
    
    		return null;
    	}
}
