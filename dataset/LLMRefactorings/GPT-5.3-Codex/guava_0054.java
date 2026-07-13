private static final int EXTRACTED_INT = 10;



      public void testGetBlocksUntilValueAvailable() throws Throwable  {

    
        assertFalse(future.isDone());
        assertFalse(future.isCancelled());
    
        ExecutorService executor = newSingleThreadExecutor();
    
        try {
          Future<Boolean> getResult = executor.submit(() -> future.get());
    
          // Release the future value.
          latch.countDown();
    
          assertTrue(getResult.get(EXTRACTED_INT, SECONDS));
        } finally {
          executor.shutdownNow();
        }
    
        assertTrue(future.isDone());
        assertFalse(future.isCancelled());
      


      }
