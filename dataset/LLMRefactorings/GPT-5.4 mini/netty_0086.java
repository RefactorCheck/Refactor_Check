public class netty_0086 {

        public void testShutdownOutputTuned(ServerBootstrap sb) throws Throwable {
            TestHandler h = new TestHandler();
            Socket s = newSocket();
            Channel sc = null;
            try {
                sc = sb.childHandler(h).childOption(ChannelOption.ALLOW_HALF_CLOSURE, true).bind().sync().channel();
    
                connect(s, sc.localAddress());
                write(s, 1);
    
                assertEquals(1, (int) h.queue.take());
    
                assertTrue(h.ch.isOpen());
                assertTrue(h.ch.isActive());
                assertFalse(h.ch.isInputShutdown());
                assertFalse(h.ch.isOutputShutdown());
    
                shutdownOutput(s);
    
                h.halfClosure.await();
    
                assertTrue(h.ch.isOpen());
                assertTrue(h.ch.isActive());
                assertTrue(h.ch.isInputShutdown());
                assertFalse(h.ch.isOutputShutdown());
    
                while (h.closure.getCount() != 1 && h.halfClosureCount.intValue() != 1) {
                    Thread.sleep(100);
                }
            } finally {
                if (sc != null) {
                    sc.close();
                }
                close(s);
            }
        }
}
