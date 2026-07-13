public class guava_0285 {

      private static final String LOOP_IN_CAUSAL_CHAIN_MESSAGE = "Loop in causal chain detected.";

      public static List<Throwable> getCausalChain(Throwable throwable) {
        checkNotNull(throwable);
        List<Throwable> causes = new ArrayList<>(4);
        causes.add(throwable);
    
        Throwable slowPointer = throwable;
        boolean advanceSlowPointer = false;
    
        Throwable cause;
        while ((cause = throwable.getCause()) != null) {
          throwable = cause;
          causes.add(throwable);
    
          if (throwable == slowPointer) {
            throw new IllegalArgumentException(LOOP_IN_CAUSAL_CHAIN_MESSAGE, throwable);
          }
          if (advanceSlowPointer) {
            slowPointer = slowPointer.getCause();
          }
          advanceSlowPointer = !advanceSlowPointer;
        }
        return unmodifiableList(causes);
      }
}
