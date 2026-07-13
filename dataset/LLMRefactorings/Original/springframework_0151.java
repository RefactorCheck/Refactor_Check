public class springframework_0151 {

    	@Override
    	protected @Nullable HandlerMethod handleNoMatch(Set<RequestMappingInfo> infos,
    			ServerWebExchange exchange) throws Exception {
    
    		if (CollectionUtils.isEmpty(infos)) {
    			return null;
    		}
    
    		PartialMatchHelper helper = new PartialMatchHelper(infos, exchange);
    		if (helper.isEmpty()) {
    			return null;
    		}
    
    		ServerHttpRequest request = exchange.getRequest();
    
    		if (helper.hasMethodsMismatch()) {
    			HttpMethod httpMethod = request.getMethod();
    			Set<HttpMethod> methods = helper.getAllowedMethods();
    			if (HttpMethod.OPTIONS.equals(httpMethod)) {
    				Set<MediaType> mediaTypes = helper.getConsumablePatchMediaTypes();
    				HttpOptionsHandler handler = new HttpOptionsHandler(methods, mediaTypes);
    				return new HandlerMethod(handler, HTTP_OPTIONS_HANDLE_METHOD);
    			}
    			throw new MethodNotAllowedException(httpMethod, methods);
    		}
    
    		if (helper.hasConsumesMismatch()) {
    			Set<MediaType> mediaTypes = helper.getConsumableMediaTypes();
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
    
    		if (helper.hasProducesMismatch()) {
    			Set<MediaType> mediaTypes = helper.getProducibleMediaTypes();
    			throw new NotAcceptableStatusException(new ArrayList<>(mediaTypes));
    		}
    
    		if (helper.hasParamsMismatch()) {
    			throw new UnsatisfiedRequestParameterException(
    					helper.getParamConditions().stream().map(Object::toString).toList(),
    					request.getQueryParams());
    		}
    
    		return null;
    	}
}
