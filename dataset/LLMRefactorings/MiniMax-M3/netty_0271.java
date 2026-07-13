import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.function.Function;
import io.netty.channel.ChannelFuture;

public class netty_0271 {

        public void testShutdownOutputAfterClosed(Bootstrap cb) throws Throwable {
            TestHandler h = new TestHandler();
            ServerSocket ss = new ServerSocket();
            Socket s = null;
            try {
                ss.bind(newSocketAddress());
                SocketChannel ch = (SocketChannel) cb.handler(h).connect(ss.getLocalSocketAddress()).sync().channel();
                assertTrue(ch.isActive());
                s = ss.accept();
    
                ch.close().syncUninterruptibly();
                assertShutdownFails(ch, SocketChannel::shutdownInput);
                assertShutdownFails(ch, SocketChannel::shutdownOutput);
            } finally {
                if (s != null) {
                    s.close();
                }
                ss.close();
            }
        }

        private void assertShutdownFails(SocketChannel ch, Function<SocketChannel, ChannelFuture> shutdownOperation) {
            try {
                shutdownOperation.apply(ch).syncUninterruptibly();
                fail();
            } catch (Throwable cause) {
                checkThrowable(cause);
            }
        }
}
