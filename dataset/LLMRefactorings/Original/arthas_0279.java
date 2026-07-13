public class arthas_0279 {

        public void registerNativeAgent(String address, String k, String v) {
            // Create zookeeper client
            ZooKeeper zk = null;
            AtomicBoolean createResult = new AtomicBoolean(false);
            try {
                zk = new ZooKeeper(address, SESSION_TIMEOUT, event -> {
                    if (event.getState() == Watcher.Event.KeeperState.SyncConnected) {
                        latch.countDown();
                        createResult.compareAndSet(false, true);
                    }
                });
                latch.await();
            } catch (Exception e) {
                logger.error("Create zookeeper client failed");
                throw new RuntimeException(e);
            } finally {
                latch.countDown();
            }
    
            if (!createResult.get()) {
                throw new RuntimeException("Create zookeeper client failed");
            }
    
            try {
                // Create a service node. If the parent node does not exist, create the parent node first
                if (zk.exists(NativeAgentConstants.NATIVE_AGENT_KEY, false) == null) {
                    zk.create(NativeAgentConstants.NATIVE_AGENT_KEY, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                }
                // The EPHEMERAL mode is used to create child nodes, which means that the nodes are automatically removed at the end of the session
                String path = zk.create(NativeAgentConstants.NATIVE_AGENT_KEY + "/" + k, v.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
                logger.info("native agent client registered at: " + path);
            } catch (KeeperException | InterruptedException e) {
                logger.error("Register native agent client failed");
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
}
