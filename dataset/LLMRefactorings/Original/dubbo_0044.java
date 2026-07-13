public class dubbo_0044 {

        @Override
        public void connected(Channel ch) throws RemotingException {
            // If the server has entered the shutdown process, reject any new connection
            if (this.isClosing() || this.isClosed()) {
                logger.warn(
                        INTERNAL_ERROR,
                        "unknown error in remoting module",
                        "",
                        "Close new channel " + ch
                                + ", cause: server is closing or has been closed. For example, receive a new connect request while in shutdown process.");
                ch.close();
                return;
            }
    
            if (accepts > 0 && getChannelsSize() > accepts) {
                logger.error(
                        INTERNAL_ERROR,
                        "unknown error in remoting module",
                        "",
                        "Close channel " + ch + ", cause: The server " + ch.getLocalAddress()
                                + " connections greater than max config " + accepts);
                ch.close();
                return;
            }
            super.connected(ch);
        }
}
