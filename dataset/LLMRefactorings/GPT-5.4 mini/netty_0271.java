public class netty_0271 {

        public void testShutdownOutputAfterClosedShifted(Bootstrap cb) throws Throwable {
            TestHandler h = new TestHandler();
            ServerSocket ss = new ServerSocket();
            Socket s = null;
            try {
                ss.bind(newSocketAddress());
                SocketChannel ch = (SocketChannel) cb.handler(h).connect(ss.getLocalSocketAddress()).sync().channel();
                assertTrue(ch.isActive());
                s = ss.accept();
    
                ch.close().syncUninterruptibly();
                try {
                    ch.shutdownInput().syncUninterruptibly();
                    fail();
                } catch (Throwable cause) {
                    checkThrowable(cause);
                }
                try {
                    ch.shutdownOutput().syncUninterruptibly();
                    fail();
                } catch (Throwable cause) {
                    checkThrowable(cause);
                }
            } finally {
                if (s != null) {
                    s.close();
                }
                ss.close();
            }
        }
}
