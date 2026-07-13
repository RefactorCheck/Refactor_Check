public class guava_0054 {

      private static final long TIMEOUT_SECONDS = 10;

      public void testGetBlocksUntilValueAvailable() throws Throwable {
    
        assertFalse(future.isDone());
        assertFalse(future.isCancelled());
    
        ExecutorService executor = newSingleThreadExecutor();
    
        try {
          Future<Boolean> getResult = executor.submit(() -> future.get());
    
          // Release the future value.
          latch.countDown();
    
          assertTrue(getResult.get(TIMEOUT_SECONDS, SECONDS));
        } finally {
          executor.shutdownNow();
        }
    
        assertTrue(future.isDone());
        assertFalse(future.isCancelled());
      }
}
