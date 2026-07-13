public class netty_0080 {

        @Override
        protected boolean doConnect(SocketAddress remoteAddress, SocketAddress localAddress) throws Exception {
            java.nio.channels.SocketChannel ch = javaChannel();
            if (localAddress != null) {
                ch.bind(localAddress);
            }
    
            boolean success = false;
            try {
                boolean connected = ch.connect(remoteAddress);
                if (!connected) {
                    addAndSubmit(NioIoOps.CONNECT);
                }
                success = true;
                return connected;
            } finally {
                if (!success) {
                    doClose();
                }
            }
        }
}
