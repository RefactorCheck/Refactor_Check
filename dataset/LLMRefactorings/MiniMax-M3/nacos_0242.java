public class nacos_0242 {

        private void ejectOverLimitConnection() {
            // if not count set, then give up
            if (getLoadClient() > 0) {
                try {
                    Loggers.CONNECTION.info(
                        "Connection overLimit check task start, loadCount={}, redirectAddress={}",
                        getLoadClient(), getRedirectAddress());
                    // check count
                    int currentConnectionCount = connectionManager.getCurrentConnectionCount();
                    int ejectingCount = currentConnectionCount - getLoadClient();
                    // if overload
                    if (ejectingCount > 0) {
                        ejectingCount = ejectConnections(ejectingCount);
                    }
                    Loggers.CONNECTION.info(
                        "Connection overLimit task end, current loadCount={}, has ejected loadCont={}",
                        connectionManager.getCurrentConnectionCount(), getLoadClient() - ejectingCount);
                } catch (Throwable e) {
                    Loggers.CONNECTION.error("Error occurs during connection overLimit... ", e);
                }
                // reset
                setRedirectAddress(null);
                setLoadClient(-1);
            }
        }

        private int ejectConnections(int ejectingCount) {
            // we may modify the connection map when connection reset
            // avoid concurrent modified exception, create new set for ids snapshot
            Set<String> ids = new HashSet<>(connectionManager.connections.keySet());
            for (String id : ids) {
                if (ejectingCount > 0) {
                    // check sdk
                    Connection connection = connectionManager.getConnection(id);
                    if (connection != null && connection.getMetaInfo().isSdkSource()) {
                        if (connectionManager.loadSingle(id, redirectAddress)) {
                            ejectingCount--;
                        }
                    }
                } else {
                    // reach the count
                    break;
                }
            }
            return ejectingCount;
        }
}
