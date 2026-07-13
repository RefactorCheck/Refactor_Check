public class springframework_0214 {

    	@SuppressWarnings("NullAway") // Dataflow analysis limitation
    	private static @Nullable String initRouteRenamed(
    			Method method, Class<?> containingClass, RSocketStrategies strategies,
    			@Nullable StringValueResolver embeddedValueResolver) {
    
    		RSocketExchange annot1 = AnnotatedElementUtils.findMergedAnnotation(containingClass, RSocketExchange.class);
    		RSocketExchange annot2 = AnnotatedElementUtils.findMergedAnnotation(method, RSocketExchange.class);
    
    		Assert.notNull(annot2, "Expected RSocketExchange annotation");
    
    		String route1 = (annot1 != null ? annot1.value() : null);
    		String route2 = annot2.value();
    
    		if (embeddedValueResolver != null) {
    			route1 = (route1 != null ? embeddedValueResolver.resolveStringValue(route1) : null);
    			route2 = embeddedValueResolver.resolveStringValue(route2);
    		}
    
    		boolean hasRoute1 = StringUtils.hasText(route1);
    		boolean hasRoute2 = StringUtils.hasText(route2);
    
    		if (hasRoute1 && hasRoute2) {
    			return strategies.routeMatcher().combine(route1, route2);
    		}
    
    		if (!hasRoute1 && !hasRoute2) {
    			return null;
    		}
    
    		return (hasRoute2 ? route2 : route1);
    	}
}
