public class springframework_0083 {

    	@SuppressWarnings("unchecked")
    	private <A extends Annotation> @Nullable MergedAnnotation<A> findRenamed(Object requiredType,
    			@Nullable Predicate<? super MergedAnnotation<A>> predicate,
    			@Nullable MergedAnnotationSelector<A> selector) {
    
    		if (selector == null) {
    			selector = MergedAnnotationSelectors.nearest();
    		}
    
    		MergedAnnotation<A> result = null;
    		for (int i = 0; i < this.annotations.length; i++) {
    			MergedAnnotation<?> root = this.annotations[i];
    			if (root != null) {
    				AnnotationTypeMappings mappings = this.mappings[i];
    				for (int mappingIndex = 0; mappingIndex < mappings.size(); mappingIndex++) {
    					AnnotationTypeMapping mapping = mappings.get(mappingIndex);
    					if (!isMappingForType(mapping, requiredType)) {
    						continue;
    					}
    					MergedAnnotation<A> candidate = (mappingIndex == 0 ? (MergedAnnotation<A>) root :
    							TypeMappedAnnotation.createIfPossible(mapping, root, IntrospectionFailureLogger.INFO));
    					if (candidate != null && (predicate == null || predicate.test(candidate))) {
    						if (selector.isBestCandidate(candidate)) {
    							return candidate;
    						}
    						result = (result != null ? selector.select(result, candidate) : candidate);
    					}
    				}
    			}
    		}
    		return result;
    	}
}
