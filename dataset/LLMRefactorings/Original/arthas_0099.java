public class arthas_0099 {

        @Override
        public List<String> listNativeAgentProxy(String address) {
            if (address == null || "".equals(address)) {
                return null;
            }
    
            // Wait for connection to be established
            try {
                ZooKeeper zooKeeper = new ZooKeeper(address, SESSION_TIMEOUT, event -> {
                    if (event.getState() == Watcher.Event.KeeperState.SyncConnected) {
                        connectedSemaphore.countDown();
                    }
                });
                connectedSemaphore.await();
    
                // Gets a list of all children of the parent node
                List<String> children = zooKeeper.getChildren(NativeAgentConstants.NATIVE_AGENT_PROXY_KEY, false);
                if (children == null || children.size() == 0) {
                    return children;
                }
    
                zooKeeper.close();
                return children;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
}
