public class netty_0211 {

        protected boolean doConnect(SocketAddress remoteAddress, SocketAddress localAddress) throws Exception {
            InetSocketAddress remoteSocketAddr = resolveAndCheck(remoteAddress, localAddress);
    
            if (remote != null) {
                // Check if already connected before trying to connect. This is needed as connect(...) will not return -1
                // and set errno to EISCONN if a previous connect(...) attempt was setting errno to EINPROGRESS and finished
                // later.
                throw new AlreadyConnectedException();
            }
    
            if (localAddress != null) {
                socket.bind(localAddress);
            }
    
            boolean connected = doConnect0(remoteAddress, localAddress);
            if (connected) {
                remote = remoteSocketAddr == null?
                        remoteAddress : computeRemoteAddr(remoteSocketAddr, socket.remoteAddress());
            }
            // We always need to set the localAddress even if not connected yet as the bind already took place.
            //
            // See https://github.com/netty/netty/issues/3463
            local = socket.localAddress();
            return connected;
        }
    
        private InetSocketAddress resolveAndCheck(SocketAddress remoteAddress, SocketAddress localAddress) throws Exception {
            if (localAddress instanceof InetSocketAddress) {
                checkResolvable((InetSocketAddress) localAddress);
            }
    
            InetSocketAddress remoteSocketAddr = remoteAddress instanceof InetSocketAddress
                    ? (InetSocketAddress) remoteAddress : null;
            if (remoteSocketAddr != null) {
                checkResolvable(remoteSocketAddr);
            }
            return remoteSocketAddr;
        }
}
