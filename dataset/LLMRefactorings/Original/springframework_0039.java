public class springframework_0039 {

    	private static List<HandlerMethodArgumentResolver> initResolvers(ArgumentResolverConfigurer customResolvers,
    			ReactiveAdapterRegistry adapterRegistry, ConfigurableApplicationContext context,
    			boolean supportDataBinding, List<HttpMessageReader<?>> readers) {
    
    		ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
    		boolean requestMappingMethod = !readers.isEmpty() && supportDataBinding;
    
    		// Annotation-based...
    		List<HandlerMethodArgumentResolver> result = new ArrayList<>(30);
    		result.add(new RequestParamMethodArgumentResolver(beanFactory, adapterRegistry, false));
    		result.add(new RequestParamMapMethodArgumentResolver(adapterRegistry));
    		result.add(new PathVariableMethodArgumentResolver(beanFactory, adapterRegistry));
    		result.add(new PathVariableMapMethodArgumentResolver(adapterRegistry));
    		result.add(new MatrixVariableMethodArgumentResolver(beanFactory, adapterRegistry));
    		result.add(new MatrixVariableMapMethodArgumentResolver(adapterRegistry));
    		if (!readers.isEmpty()) {
    			result.add(new RequestBodyMethodArgumentResolver(readers, adapterRegistry));
    			result.add(new RequestPartMethodArgumentResolver(readers, adapterRegistry));
    		}
    		if (supportDataBinding) {
    			result.add(new ModelAttributeMethodArgumentResolver(adapterRegistry, false));
    		}
    		result.add(new RequestHeaderMethodArgumentResolver(beanFactory, adapterRegistry));
    		result.add(new RequestHeaderMapMethodArgumentResolver(adapterRegistry));
    		result.add(new CookieValueMethodArgumentResolver(beanFactory, adapterRegistry));
    		result.add(new ExpressionValueMethodArgumentResolver(beanFactory, adapterRegistry));
    		result.add(new SessionAttributeMethodArgumentResolver(beanFactory, adapterRegistry));
    		result.add(new RequestAttributeMethodArgumentResolver(beanFactory, adapterRegistry));
    
    		// Type-based...
    		if (!readers.isEmpty()) {
    			result.add(new HttpEntityMethodArgumentResolver(readers, adapterRegistry));
    		}
    		result.add(new ModelMethodArgumentResolver(adapterRegistry));
    		if (supportDataBinding) {
    			result.add(new ErrorsMethodArgumentResolver(adapterRegistry));
    		}
    		result.add(new ServerWebExchangeMethodArgumentResolver(adapterRegistry));
    		result.add(new PrincipalMethodArgumentResolver(adapterRegistry));
    		if (requestMappingMethod) {
    			result.add(new SessionStatusMethodArgumentResolver());
    		}
    		result.add(new WebSessionMethodArgumentResolver(adapterRegistry));
    		result.add(new ApiVersionMethodArgumentResolver());
    		if (KotlinDetector.isKotlinPresent()) {
    			result.add(new ContinuationHandlerMethodArgumentResolver());
    		}
    
    		// Custom...
    		result.addAll(customResolvers.getCustomResolvers());
    
    		// Catch-all...
    		result.add(new RequestParamMethodArgumentResolver(beanFactory, adapterRegistry, true));
    		if (supportDataBinding) {
    			result.add(new ModelAttributeMethodArgumentResolver(adapterRegistry, true));
    		}
    
    		return result;
    	}
}
