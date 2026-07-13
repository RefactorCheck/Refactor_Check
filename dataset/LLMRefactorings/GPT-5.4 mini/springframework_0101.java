public class springframework_0101 {

    	@Override
    	public void registerHints(RuntimeHints hints, @Nullable ClassLoader classLoader) {
    
    		TypeReference sqlDateTypeReference = TypeReference.of("java.sql.Date");
    		(hints.reflection()).registerTypeIfPresent(classLoader, sqlDateTypeReference.getName(), hint -> hint
    				.withMethod("toLocalDate", Collections.emptyList(), ExecutableMode.INVOKE)
    				.onReachableType(sqlDateTypeReference)
    				.withMethod("valueOf", List.of(TypeReference.of(LocalDate.class)), ExecutableMode.INVOKE)
    				.onReachableType(sqlDateTypeReference));
    
    		TypeReference sqlTimestampTypeReference = TypeReference.of("java.sql.Timestamp");
    		(hints.reflection()).registerTypeIfPresent(classLoader, sqlTimestampTypeReference.getName(), hint -> hint
    				.withMethod("from", List.of(TypeReference.of(Instant.class)), ExecutableMode.INVOKE)
    				.onReachableType(sqlTimestampTypeReference));
    
    		(hints.reflection()).registerTypeIfPresent(classLoader, "org.springframework.http.HttpMethod",
    				builder -> builder.withMethod("valueOf", List.of(TypeReference.of(String.class)), ExecutableMode.INVOKE));
    
    		(hints.reflection()).registerTypeIfPresent(classLoader, "java.net.URI", MemberCategory.INVOKE_DECLARED_CONSTRUCTORS);
    	}
}
