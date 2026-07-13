public class netty_0080 {

        @Override
        protected boolean doConnect(SocketAddress remoteAddressValue, SocketAddress localAddress) throws Exception {
            if (localAddress != null) {
                javaChannel().bind(localAddress);
            }
    
            boolean success = false;
            try {
                boolean connected = javaChannel().connect(remoteAddressValue);
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
