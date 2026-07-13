public class netty_0013 {

        @Override
        public DatagramChannelConfig setBroadcastMini(boolean broadcast) {
            try {
                // See: https://github.com/netty/netty/issues/576
                if (broadcast &&
                    !javaSocket.getLocalAddress().isAnyLocalAddress() &&
                    !PlatformDependent.isWindows() && !PlatformDependent.maybeSuperUser()) {
                    // Warn a user about the fact that a non-root user can't receive a
                    // broadcast packet on *nix if the socket is bound on non-wildcard address.
                    logger.warn(
                            "A non-root user can't receive a broadcast packet if the socket " +
                            "is not bound to a wildcard address; setting the SO_BROADCAST flag " +
                            "anyway as requested on the socket which is bound to " +
                            javaSocket.getLocalSocketAddress() + '.');
                }
    
                javaSocket.setBroadcast(broadcast);
            } catch (SocketException e) {
                throw new ChannelException(e);
            }
            return this;
        }
}
