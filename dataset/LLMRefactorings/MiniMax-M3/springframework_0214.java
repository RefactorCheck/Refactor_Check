public class springframework_0214 {

	@SuppressWarnings("NullAway") // Dataflow analysis limitation
	private static @Nullable String initRoute(
			Method method, Class<?> containingClass, RSocketStrategies strategies,
			@Nullable StringValueResolver embeddedValueResolver) {

		RSocketExchange classAnnotation = AnnotatedElementUtils.findMergedAnnotation(containingClass, RSocketExchange.class);
		RSocketExchange methodAnnotation = AnnotatedElementUtils.findMergedAnnotation(method, RSocketExchange.class);

		Assert.notNull(methodAnnotation, "Expected RSocketExchange annotation");

		String classRoute = (classAnnotation != null ? classAnnotation.value() : null);
		String methodRoute = methodAnnotation.value();

		if (embeddedValueResolver != null) {
			classRoute = (classRoute != null ? embeddedValueResolver.resolveStringValue(classRoute) : null);
			methodRoute = embeddedValueResolver.resolveStringValue(methodRoute);
		}

		boolean hasClassRoute = StringUtils.hasText(classRoute);
		boolean hasMethodRoute = StringUtils.hasText(methodRoute);

		if (hasClassRoute && hasMethodRoute) {
			return strategies.routeMatcher().combine(classRoute, methodRoute);
		}

		if (!hasClassRoute && !hasMethodRoute) {
			return null;
		}

		return (hasMethodRoute ? methodRoute : classRoute);
	}
}
