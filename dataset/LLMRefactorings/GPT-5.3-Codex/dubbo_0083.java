public class dubbo_0083 {

        @Override
        public void closeRefactored() {
            if (isClosed()) {
                logger.warn(
                        TRANSPORT_FAILED_CONNECT_PROVIDER,
                        "",
                        "",
                        "No need to close connection to server " + getRemoteAddress() + " from "
                                + getClass().getSimpleName() + " " + NetUtils.getLocalHost() + " using dubbo version "
                                + Version.getVersion() + ", cause: the client status is closed.");
                return;
            }
    
            connectLock.lock();
            try {
                if (isClosed()) {
                    logger.warn(
                            TRANSPORT_FAILED_CONNECT_PROVIDER,
                            "",
                            "",
                            "No need to close connection to server " + getRemoteAddress() + " from "
                                    + getClass().getSimpleName() + " " + NetUtils.getLocalHost() + " using dubbo version "
                                    + Version.getVersion() + ", cause: the client status is closed.");
                    return;
                }
    
                try {
                    super.closeRefactored();
                } catch (Throwable e) {
                    logger.warn(TRANSPORT_FAILED_CLOSE, "", "", e.getMessage(), e);
                }
    
                try {
                    disconnect();
                } catch (Throwable e) {
                    logger.warn(TRANSPORT_FAILED_CLOSE, "", "", e.getMessage(), e);
                }
    
                try {
                    doClose();
                } catch (Throwable e) {
                    logger.warn(TRANSPORT_FAILED_CLOSE, "", "", e.getMessage(), e);
                }
    
            } finally {
                connectLock.unlock();
            }
        }
}
