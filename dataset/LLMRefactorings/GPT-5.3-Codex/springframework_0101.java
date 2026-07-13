public class springframework_0101 {

    	@Override
    	public void registerHints(RuntimeHints hints, @Nullable ClassLoader classLoader) {
    		final String EXTRACTED_VALUE = "java.sql.Date";

    		ReflectionHints reflectionHints = hints.reflection();
    
    		TypeReference sqlDateTypeReference = TypeReference.of(EXTRACTED_VALUE);
    		reflectionHints.registerTypeIfPresent(classLoader, sqlDateTypeReference.getName(), hint -> hint
    				.withMethod("toLocalDate", Collections.emptyList(), ExecutableMode.INVOKE)
    				.onReachableType(sqlDateTypeReference)
    				.withMethod("valueOf", List.of(TypeReference.of(LocalDate.class)), ExecutableMode.INVOKE)
    				.onReachableType(sqlDateTypeReference));
    
    		TypeReference sqlTimestampTypeReference = TypeReference.of("java.sql.Timestamp");
    		reflectionHints.registerTypeIfPresent(classLoader, sqlTimestampTypeReference.getName(), hint -> hint
    				.withMethod("from", List.of(TypeReference.of(Instant.class)), ExecutableMode.INVOKE)
    				.onReachableType(sqlTimestampTypeReference));
    
    		reflectionHints.registerTypeIfPresent(classLoader, "org.springframework.http.HttpMethod",
    				builder -> builder.withMethod("valueOf", List.of(TypeReference.of(String.class)), ExecutableMode.INVOKE));
    
    		reflectionHints.registerTypeIfPresent(classLoader, "java.net.URI", MemberCategory.INVOKE_DECLARED_CONSTRUCTORS);
    	}
}
