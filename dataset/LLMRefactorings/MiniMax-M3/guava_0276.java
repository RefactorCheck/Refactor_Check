public class guava_0276 {

      private static final String LOOP_IN_CAUSAL_CHAIN_MESSAGE = "Loop in causal chain detected.";

      public static Throwable getRootCause(Throwable throwable) {
        // Keep a second pointer that slowly walks the causal chain. If the fast pointer ever catches
        // the slower pointer, then there's a loop.
        Throwable slowPointer = throwable;
        boolean advanceSlowPointer = false;
    
        Throwable cause;
        while ((cause = throwable.getCause()) != null) {
          throwable = cause;
    
          if (throwable == slowPointer) {
            throw new IllegalArgumentException(LOOP_IN_CAUSAL_CHAIN_MESSAGE, throwable);
          }
          if (advanceSlowPointer) {
            slowPointer = slowPointer.getCause();
          }
          advanceSlowPointer = !advanceSlowPointer; // only advance every other iteration
        }
        return throwable;
      }
}
