private boolean cleanUp(Reference<?> firstReference)  {

        if (getFinalizeReferentMethod() == null) {
          return false;
        }
    
        if (!finalizeReference(firstReference, finalizeReferentMethod)) {
          return false;
        }
    
        /*
         * Loop as long as we have references available so as not to waste CPU looking up the Method
         * over and over again.
         */
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
