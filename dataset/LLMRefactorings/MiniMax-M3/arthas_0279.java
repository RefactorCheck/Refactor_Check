public class arthas_0279 {

    public void registerNativeAgent(String address, String k, String v) {
        ZooKeeper zk = createZooKeeperClient(address);

        try {
            if (zk.exists(NativeAgentConstants.NATIVE_AGENT_KEY, false) == null) {
                zk.create(NativeAgentConstants.NATIVE_AGENT_KEY, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
            String path = zk.create(NativeAgentConstants.NATIVE_AGENT_KEY + "/" + k, v.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            logger.info("native agent client registered at: " + path);
        } catch (KeeperException | InterruptedException e) {
            logger.error("Register native agent client failed");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private ZooKeeper createZooKeeperClient(String address) {
        AtomicBoolean createResult = new AtomicBoolean(false);
        try {
            ZooKeeper zk = new ZooKeeper(address, SESSION_TIMEOUT, event -> {
                if (event.getState() == Watcher.Event.KeeperState.SyncConnected) {
                    latch.countDown();
                    createResult.compareAndSet(false, true);
                }
            });
            latch.await();
            if (!createResult.get()) {
                throw new RuntimeException("Create zookeeper client failed");
            }
            return zk;
        } catch (Exception e) {
            logger.error("Create zookeeper client failed");
            throw new RuntimeException(e);
        } finally {
            latch.countDown();
        }
    }
}
