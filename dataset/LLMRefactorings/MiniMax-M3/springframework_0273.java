public class springframework_0273 {

    private static final String UNSUPPORTED_CLASS_FILTER_MESSAGE = "Filter type not supported with Class value: ";
    private static final String UNSUPPORTED_PATTERN_FILTER_MESSAGE = "Filter type not supported with String pattern: ";

    public static List<TypeFilter> createTypeFiltersFor(AnnotationAttributes filterAttributes, Environment environment,
            ResourceLoader resourceLoader, BeanDefinitionRegistry registry) {

        List<TypeFilter> typeFilters = new ArrayList<>();
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
                    throw new IllegalArgumentException(UNSUPPORTED_CLASS_FILTER_MESSAGE + filterType);
            }
        }

        for (String expression : filterAttributes.getStringArray("pattern")) {
            switch (filterType) {
                case ASPECTJ -> typeFilters.add(new AspectJTypeFilter(expression, resourceLoader.getClassLoader()));
                case REGEX -> typeFilters.add(new RegexPatternTypeFilter(Pattern.compile(expression)));
                default ->
                    throw new IllegalArgumentException(UNSUPPORTED_PATTERN_FILTER_MESSAGE + filterType);
            }
        }

        return typeFilters;
    }
}
