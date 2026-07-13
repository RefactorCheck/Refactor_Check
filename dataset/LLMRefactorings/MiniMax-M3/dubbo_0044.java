public class dubbo_0044 {

    @Override
    public void connected(Channel ch) throws RemotingException {
        if (this.isClosing() || this.isClosed()) {
            rejectChannel(ch, "Close new channel " + ch
                    + ", cause: server is closing or has been closed. For example, receive a new connect request while in shutdown process.", true);
            return;
        }

        if (accepts > 0 && getChannelsSize() > accepts) {
            rejectChannel(ch, "Close channel " + ch + ", cause: The server " + ch.getLocalAddress()
                    + " connections greater than max config " + accepts, false);
            return;
        }
        super.connected(ch);
    }

    private void rejectChannel(Channel ch, String reason, boolean asWarning) {
        if (asWarning) {
            logger.warn(INTERNAL_ERROR, "unknown error in remoting module", "", reason);
        } else {
            logger.error(INTERNAL_ERROR, "unknown error in remoting module", "", reason);
        }
        ch.close();
    }
}
