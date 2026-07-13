public class guava_0054 {

      public void testGetBlocksUntilValueAvailable() throws Throwable {
    
        assertFalse(future.isDone());
        assertFalse(future.isCancelled());
    
        ExecutorService executor = newSingleThreadExecutor();
    
        try {
          Future<Boolean> getResult = executor.submit(() -> future.get());
    
          // Release the future value.
          latch.countDown();
    
          assertTrue(getResult.get(10, SECONDS));
        } finally {
          executor.shutdownNow();
        }
    
        assertTrue(future.isDone());
        assertFalse(future.isCancelled());
      }
}
