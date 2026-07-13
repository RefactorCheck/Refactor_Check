public class springframework_0247 {

    @Override
    public boolean tryAdvance(Consumer<? super MergedAnnotation<A>> action) {
        int annotationResult = findAnnotationWithLowestDistance();
        if (annotationResult != -1) {
            MergedAnnotation<A> mergedAnnotation = createMergedAnnotationIfPossible(
                    annotationResult, this.mappingCursors[annotationResult]);
            this.mappingCursors[annotationResult]++;
            if (mergedAnnotation == null) {
                return tryAdvance(action);
            }
            action.accept(mergedAnnotation);
            return true;
        }
        return false;
    }

    private int findAnnotationWithLowestDistance() {
        int lowestDistance = Integer.MAX_VALUE;
        int annotationResult = -1;
        for (int annotationIndex = 0; annotationIndex < annotations.length; annotationIndex++) {
            AnnotationTypeMapping mapping = getNextSuitableMapping(annotationIndex);
            if (mapping != null && mapping.getDistance() < lowestDistance) {
                annotationResult = annotationIndex;
                lowestDistance = mapping.getDistance();
            }
            if (lowestDistance == 0) {
                break;
            }
        }
        return annotationResult;
    }
}
