public class netty_0245 {

    private void testShutdownSoLingerNoAssertError0(Bootstrap cb, boolean output) throws Throwable {
        ServerSocket ss = new ServerSocket();
        Socket s = null;

        ChannelFuture cf = null;
        try {
            ss.bind(newSocketAddress());
            cf = cb.option(ChannelOption.SO_LINGER, 1).handler(new ChannelInboundHandlerAdapter())
                    .connect(ss.getLocalSocketAddress()).sync();
            s = ss.accept();

            cf.sync();

            SocketChannel channel = (SocketChannel) cf.channel();
            if (output) {
                channel.shutdownOutput().sync();
            } else {
                channel.shutdown().sync();
            }
        } finally {
            if (s != null) {
                s.close();
            }
            if (cf != null) {
                cf.channel().close();
            }
            ss.close();
        }
    }
}
