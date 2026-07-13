public class netty_0013 {

        @Override
        public DatagramChannelConfig setBroadcast(boolean broadcastValue) {
            try {
                // See: https://github.com/netty/netty/issues/576
                if (broadcastValue &&
                    !javaSocket.getLocalAddress().isAnyLocalAddress() &&
                    !PlatformDependent.isWindows() && !PlatformDependent.maybeSuperUser()) {
                    // Warn a user about the fact that a non-root user can't receive a
                    // broadcastValue packet on *nix if the socket is bound on non-wildcard address.
                    logger.warn(
                            "A non-root user can't receive a broadcastValue packet if the socket " +
                            "is not bound to a wildcard address; setting the SO_BROADCAST flag " +
                            "anyway as requested on the socket which is bound to " +
                            javaSocket.getLocalSocketAddress() + '.');
                }
    
                javaSocket.setBroadcast(broadcastValue);
            } catch (SocketException e) {
                throw new ChannelException(e);
            }
            return this;
        }
}
