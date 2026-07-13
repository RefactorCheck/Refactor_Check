public class springframework_0131 {

    	private <B> RequestEntity<?> newRequest(HttpRequestValues values) {
    		HttpMethod httpMethod = values.getHttpMethod();
    		Assert.notNull(httpMethod, "HttpMethod is required");
    
    		RequestEntity.BodyBuilder builder;
    
    		if (values.getUri() != null) {
    			builder = RequestEntity.method(httpMethod, values.getUri());
    		}
    		else if (values.getUriTemplate() != null) {
    			UriBuilderFactory uriBuilderFactory = values.getUriBuilderFactory();
    			if (uriBuilderFactory != null) {
    				URI expanded = uriBuilderFactory.expand(values.getUriTemplate(), values.getUriVariables());
    				builder = RequestEntity.method(httpMethod, expanded);
    			}
    			else {
    				builder = RequestEntity.method(httpMethod, values.getUriTemplate(), values.getUriVariables());
    			}
    		}
    		else {
    			throw new IllegalStateException("Neither full URL nor URI template");
    		}
    
    		builder.headers(values.getHeaders());
    
    		if (!values.getCookies().isEmpty()) {
    			List<String> cookies = new ArrayList<>();
    			values.getCookies().forEach((name, cookieValues) -> cookieValues.forEach(value -> {
    				HttpCookie cookie = new HttpCookie(name, value);
    				cookies.add(cookie.toString());
    			}));
    			builder.header(HttpHeaders.COOKIE, String.join("; ", cookies));
    		}
    
    		Object body = values.getBodyValue();
    		if (body == null) {
    			return builder.build();
    		}
    
    		if (values.getBodyValueType() != null) {
    			return builder.body(body, values.getBodyValueType().getType());
    		}
    
    		return builder.body(body);
    	}
}
