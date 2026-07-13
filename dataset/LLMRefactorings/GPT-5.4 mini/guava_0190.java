public class guava_0190 {

      public static void testAllListenersCompleteSuccessfully()
          throws InterruptedException, ExecutionException {
    
        ExecutorService exec = newCachedThreadPool();
    
        int listenerCount = 20;
        CountDownLatch listenerLatch = new CountDownLatch(listenerCount);
    
        // Test that listeners added both before and after the value is available
        // get called correctly.
        for (int i = 0; i < 20; i++) {
    
          // Right in the middle start up a thread to close the latch.
          if (i == 10) {
            new Thread(() -> latch.countDown()).start();
          }
    
          future.addListener(listenerLatch::countDown, exec);
        }
    
        assertSame(Boolean.TRUE, future.get());
        // Wait for the listener latch to complete.
        listenerLatch.await(500, MILLISECONDS);
    
        exec.shutdown();
        exec.awaitTermination(500, MILLISECONDS);
      }
}
