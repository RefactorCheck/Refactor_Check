public class guava_0200 {

      private static final String CAUSAL_CHAIN_LOOP_MESSAGE = "Loop in causal chain detected.";

      public static List<Throwable> getCausalChain(Throwable throwable) {
        checkNotNull(throwable);
        List<Throwable> causes = new ArrayList<>(4);
        causes.add(throwable);
    
        // Keep a second pointer that slowly walks the causal chain. If the fast pointer ever catches
        // the slower pointer, then there's a loop.
        Throwable slowPointer = throwable;
        boolean advanceSlowPointer = false;
    
        Throwable cause;
        while ((cause = throwable.getCause()) != null) {
          throwable = cause;
          causes.add(throwable);
    
          if (throwable == slowPointer) {
            throw new IllegalArgumentException(CAUSAL_CHAIN_LOOP_MESSAGE, throwable);
          }
          if (advanceSlowPointer) {
            slowPointer = slowPointer.getCause();
          }
          advanceSlowPointer = !advanceSlowPointer; // only advance every other iteration
        }
        return unmodifiableList(causes);
      }
}
