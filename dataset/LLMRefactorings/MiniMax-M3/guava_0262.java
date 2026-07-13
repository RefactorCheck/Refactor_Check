public class guava_0262 {

      private boolean cleanUp(Reference<?> firstReference) {
        Method finalizeReferentMethod = getFinalizeReferentMethod();
        if (finalizeReferentMethod == null) {
          return false;
        }
    
        if (!finalizeReference(firstReference, finalizeReferentMethod)) {
          return false;
        }
    
        return cleanUpQueue(finalizeReferentMethod);
      }

      private boolean cleanUpQueue(Method finalizeReferentMethod) {
        while (true) {
          Reference<?> furtherReference = queue.poll();
          if (furtherReference == null) {
            return true;
          }
          if (!finalizeReference(furtherReference, finalizeReferentMethod)) {
            return false;
          }
        }
      }
}
