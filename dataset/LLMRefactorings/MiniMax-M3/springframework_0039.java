public class springframework_0039 {

	private static List<HandlerMethodArgumentResolver> initResolvers(ArgumentResolverConfigurer customResolvers,
			ReactiveAdapterRegistry adapterRegistry, ConfigurableApplicationContext context,
			boolean supportDataBinding, List<HttpMessageReader<?>> readers) {

		ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
		boolean requestMappingMethod = !readers.isEmpty() && supportDataBinding;

		List<HandlerMethodArgumentResolver> result = new ArrayList<>(30);
		addAnnotationBasedResolvers(result, beanFactory, adapterRegistry, supportDataBinding, readers);
		addTypeBasedResolvers(result, adapterRegistry, supportDataBinding, readers, requestMappingMethod);
		result.addAll(customResolvers.getCustomResolvers());
		addCatchAllResolvers(result, beanFactory, adapterRegistry, supportDataBinding);
		return result;
	}

	private static void addAnnotationBasedResolvers(List<HandlerMethodArgumentResolver> result,
			ConfigurableListableBeanFactory beanFactory, ReactiveAdapterRegistry adapterRegistry,
			boolean supportDataBinding, List<HttpMessageReader<?>> readers) {
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
	}

	private static void addTypeBasedResolvers(List<HandlerMethodArgumentResolver> result,
			ReactiveAdapterRegistry adapterRegistry, boolean supportDataBinding,
			List<HttpMessageReader<?>> readers, boolean requestMappingMethod) {
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
	}

	private static void addCatchAllResolvers(List<HandlerMethodArgumentResolver> result,
			ConfigurableListableBeanFactory beanFactory, ReactiveAdapterRegistry adapterRegistry,
			boolean supportDataBinding) {
		result.add(new RequestParamMethodArgumentResolver(beanFactory, adapterRegistry, true));
		if (supportDataBinding) {
			result.add(new ModelAttributeMethodArgumentResolver(adapterRegistry, true));
		}
	}
}
