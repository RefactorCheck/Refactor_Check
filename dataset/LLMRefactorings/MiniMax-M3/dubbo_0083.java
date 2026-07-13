public class dubbo_0083 {

        @Override
        public void close() {
            if (isClosed()) {
                logAlreadyClosedWarning();
                return;
            }
    
            connectLock.lock();
            try {
                if (isClosed()) {
                    logAlreadyClosedWarning();
                    return;
                }
    
                try {
                    super.close();
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

        private void logAlreadyClosedWarning() {
            logger.warn(
                    TRANSPORT_FAILED_CONNECT_PROVIDER,
                    "",
                    "",
                    "No need to close connection to server " + getRemoteAddress() + " from "
                            + getClass().getSimpleName() + " " + NetUtils.getLocalHost() + " using dubbo version "
                            + Version.getVersion() + ", cause: the client status is closed.");
        }
}
