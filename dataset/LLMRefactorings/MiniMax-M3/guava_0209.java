public class guava_0209 {

      public void testListenersNotifiedOnError() throws Exception {
        CountDownLatch successLatch = new CountDownLatch(1);
        CountDownLatch listenerLatch = new CountDownLatch(1);
    
        ExecutorService exec = newCachedThreadPool();
    
        future.addListener(listenerLatch::countDown, exec);
    
        new Thread(() -> verifyCancellation(successLatch)).start();
    
        future.cancel(true);
    
        assertTrue(future.isCancelled());
        assertTrue(future.isDone());
    
        assertTrue(successLatch.await(200, MILLISECONDS));
        assertTrue(listenerLatch.await(200, MILLISECONDS));
    
        latch.countDown();
    
        exec.shutdown();
        exec.awaitTermination(100, MILLISECONDS);
      }
      
      private void verifyCancellation(CountDownLatch successLatch) {
        assertThrows(CancellationException.class, future::get);
        successLatch.countDown();
      }
}
