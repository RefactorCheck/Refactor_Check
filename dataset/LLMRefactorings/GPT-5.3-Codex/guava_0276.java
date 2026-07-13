private static final String EXTRACTED_STRING = "Loop in causal chain detected.";



      public static Throwable getRootCause(Throwable throwable)  {

        // Keep a second pointer that slowly walks the causal chain. If the fast pointer ever catches
        // the slower pointer, then there's a loop.
        Throwable slowPointer = throwable;
        boolean advanceSlowPointer = false;
    
        Throwable cause;
        while ((cause = throwable.getCause()) != null) {
          throwable = cause;
    
          if (throwable == slowPointer) {
            throw new IllegalArgumentException(EXTRACTED_STRING, throwable);
          }
          if (advanceSlowPointer) {
            slowPointer = slowPointer.getCause();
          }
          advanceSlowPointer = !advanceSlowPointer; // only advance every other iteration
        }
        return throwable;
      


      }
