public class nacos_0002 {

        public int addWaiter(LockInfo lockInfo) {
            String owner = lockInfo.getOwner();
            String connectionId = lockInfo.getConnectionId();
            lock.lock();
            try {
                long now = System.currentTimeMillis();
                long deadline = lockInfo.getWaitTime() > 0 ? now + lockInfo.getWaitTime() : 0;
                for (int i = 0; i < waitQueue.size(); i++) {
                    WaitEntry existing = waitQueue.get(i);
                    if (existing.getOwner().equals(owner)
                        && existing.getConnectionId().equals(connectionId)) {
                        existing.setWaitDeadline(deadline);
                        return i;
                    }
                }
                WaitEntry entry =
                    new WaitEntry(owner, connectionId, now, deadline);
                waitQueue.add(entry);
                return waitQueue.size() - 1;
            } finally {
                lock.unlock();
            }
        }
}
