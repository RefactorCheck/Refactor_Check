public class arthas_0099 {

        @Override
        public List<String> listNativeAgentProxy(String address) {
            if (address == null || "".equals(address)) {
                return null;
            }
            return fetchChildrenList(address);
        }

        private List<String> fetchChildrenList(String address) {
            try {
                ZooKeeper zooKeeper = new ZooKeeper(address, SESSION_TIMEOUT, event -> {
                    if (event.getState() == Watcher.Event.KeeperState.SyncConnected) {
                        connectedSemaphore.countDown();
                    }
                });
                connectedSemaphore.await();

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
