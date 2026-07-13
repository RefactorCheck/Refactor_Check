public class dubbo_0146 {

    public void disconnect() {
        connectLock.lock();
        try {
            performDisconnection();
        } finally {
            connectLock.unlock();
        }
    }

    private void performDisconnection() {
        try {
            Channel channel = getChannel();
            if (channel != null) {
                channel.close();
            }
        } catch (Throwable e) {
            logger.warn(TRANSPORT_FAILED_CLOSE, "", "", e.getMessage(), e);
        }
        try {
            doDisConnect();
        } catch (Throwable e) {
            logger.warn(TRANSPORT_FAILED_CLOSE, "", "", e.getMessage(), e);
        }
    }
}
