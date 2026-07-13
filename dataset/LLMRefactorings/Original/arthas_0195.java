public class arthas_0195 {

        @Override
        public void close() throws IOException
        {
            // Completely disregard the fact thread may still be running.
            // We can't afford to block on this close by waiting for
            // thread to terminate because few if any JVM's will actually
            // interrupt a system read() from the interrupt() method.
            super.close();
    
            synchronized (__queue)
            {
                __hasReachedEOF = true;
                __isClosed      = true;
    
                if (__thread != null && __thread.isAlive())
                {
                    __thread.interrupt();
                }
    
                __queue.notifyAll();
            }
    
        }
}
