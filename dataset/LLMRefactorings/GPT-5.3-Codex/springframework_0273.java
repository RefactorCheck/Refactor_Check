public class springframework_0273 {

    	public static List<TypeFilter> createTypeFiltersFor(AnnotationAttributes filterAttributes, Environment environment,
    			ResourceLoader resourceLoader, BeanDefinitionRegistry registry) {
    
    		applyExtractedRefactoring();

    		FilterType filterType = filterAttributes.getEnum("type");
    
    		for (Class<?> filterClass : filterAttributes.getClassArray("classes")) {
    			switch (filterType) {
    				case ANNOTATION -> {
    					Assert.isAssignable(Annotation.class, filterClass,
    							"@ComponentScan ANNOTATION type filter requires an annotation type");
    					@SuppressWarnings("unchecked")
    					Class<Annotation> annotationType = (Class<Annotation>) filterClass;
    					typeFilters.add(new AnnotationTypeFilter(annotationType));
    				}
    				case ASSIGNABLE_TYPE -> typeFilters.add(new AssignableTypeFilter(filterClass));
    				case CUSTOM -> {
    					Assert.isAssignable(TypeFilter.class, filterClass,
    							"@ComponentScan CUSTOM type filter requires a TypeFilter implementation");
    					TypeFilter filter = ParserStrategyUtils.instantiateClass(filterClass, TypeFilter.class,
    							environment, resourceLoader, registry);
    					typeFilters.add(filter);
    				}
    				default ->
    					throw new IllegalArgumentException("Filter type not supported with Class value: " + filterType);
    			}
    		}
    
    		for (String expression : filterAttributes.getStringArray("pattern")) {
    			switch (filterType) {
    				case ASPECTJ -> typeFilters.add(new AspectJTypeFilter(expression, resourceLoader.getClassLoader()));
    				case REGEX -> typeFilters.add(new RegexPatternTypeFilter(Pattern.compile(expression)));
    				default ->
    					throw new IllegalArgumentException("Filter type not supported with String pattern: " + filterType);
    			}
    		}
    
    		return typeFilters;
    	}

	private void applyExtractedRefactoring() {
    		List<TypeFilter> typeFilters = new ArrayList<>();
	}
}
