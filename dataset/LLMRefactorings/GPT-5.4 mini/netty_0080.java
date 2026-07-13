public class netty_0080 {

        @Override
        protected boolean doConnectRefactored(SocketAddress remoteAddress, SocketAddress localAddress) throws Exception {
            if (localAddress != null) {
                javaChannel().bind(localAddress);
            }
    
            boolean success = false;
            try {
                boolean connected = javaChannel().connect(remoteAddress);
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
