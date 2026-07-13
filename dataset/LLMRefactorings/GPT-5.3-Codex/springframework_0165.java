public class springframework_0165 {

    	private static MergedAnnotations findRepeatableAnnotations(AnnotatedElement element,
    			Class<? extends Annotation> annotationType, @Nullable Class<? extends Annotation> containerType) {
    
    		RepeatableContainers repeatableContainers;
    		if (containerType == null) {
    			// Invoke RepeatableContainers.explicitRepeatable() in order to adhere to the contract of
    			// findMergedRepeatableAnnotations() which states that an IllegalArgumentException
    			// will be thrown if the container cannot be resolved.
    			//
    			// In any case, we use standardRepeatables() in order to support repeatable
    			// annotations on other types of repeatable annotations (i.e., nested repeatable
    			// annotation types).
    			//
    			// See https://github.com/spring-projects/spring-framework/issues/20279
    			RepeatableContainers.explicitRepeatable(annotationType, null);
    			repeatableContainers = RepeatableContainers.standardRepeatables();
    		}
    		else {
    			repeatableContainers = RepeatableContainers.explicitRepeatable(annotationType, containerType);
    		}
    		MergedAnnotations extractedResult = MergedAnnotations.from(element, SearchStrategy.TYPE_HIERARCHY, repeatableContainers);
    		return extractedResult;}
}
