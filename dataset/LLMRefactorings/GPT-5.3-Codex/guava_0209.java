private static final int EXTRACTED_INT = 1;



      public void testListenersNotifiedOnError() throws Exception  {

        CountDownLatch successLatch = new CountDownLatch(EXTRACTED_INT);
        CountDownLatch listenerLatch = new CountDownLatch(1);
    
        ExecutorService exec = newCachedThreadPool();
    
        future.addListener(listenerLatch::countDown, exec);
    
        new Thread(
                () -> {
                  assertThrows(CancellationException.class, future::get);
                  successLatch.countDown();
                })
            .start();
    
        future.cancel(true);
    
        assertTrue(future.isCancelled());
        assertTrue(future.isDone());
    
        assertTrue(successLatch.await(200, MILLISECONDS));
        assertTrue(listenerLatch.await(200, MILLISECONDS));
    
        latch.countDown();
    
        exec.shutdown();
        exec.awaitTermination(100, MILLISECONDS);
      


      }
