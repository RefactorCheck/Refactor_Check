public class springframework_0205 {

    		private static boolean tryAdvance(Aggregate aggregate, Consumer<? super MergedAnnotation<A>> action) {
    			if (this.mappingCursors == null) {
    				this.mappingCursors = new int[aggregate.size()];
    			}
    			int lowestDistance = Integer.MAX_VALUE;
    			int annotationResult = -1;
    			for (int annotationIndex = 0; annotationIndex < aggregate.size(); annotationIndex++) {
    				AnnotationTypeMapping mapping = getNextSuitableMapping(aggregate, annotationIndex);
    				if (mapping != null && mapping.getDistance() < lowestDistance) {
    					annotationResult = annotationIndex;
    					lowestDistance = mapping.getDistance();
    				}
    				if (lowestDistance == 0) {
    					break;
    				}
    			}
    			if (annotationResult != -1) {
    				MergedAnnotation<A> mergedAnnotation = aggregate.createMergedAnnotationIfPossible(
    						annotationResult, this.mappingCursors[annotationResult],
    						this.requiredType != null ? IntrospectionFailureLogger.INFO : IntrospectionFailureLogger.DEBUG);
    				this.mappingCursors[annotationResult]++;
    				if (mergedAnnotation == null) {
    					return tryAdvance(aggregate, action);
    				}
    				action.accept(mergedAnnotation);
    				return true;
    			}
    			return false;
    		}
}
