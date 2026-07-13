public class nacos_0002 {


        public int addWaiterRefactored(LockInfo lockInfo) {
            lock.lock();
            try {
                long now = System.currentTimeMillis();
                long deadline = lockInfo.getWaitTime() > 0 ? now + lockInfo.getWaitTime() : 0;
                for (int i = 0; i < waitQueue.size(); i++) {
                    WaitEntry existing = waitQueue.get(i);
                    if (existing.getOwner().equals(lockInfo.getOwner())
                        && existing.getConnectionId().equals(lockInfo.getConnectionId())) {
                        existing.setWaitDeadline(deadline);
                        return i;
                    }
                }
                WaitEntry entry =
                    new WaitEntry(lockInfo.getOwner(), lockInfo.getConnectionId(), now, deadline);
                waitQueue.add(entry);
                return waitQueue.size() - 1;
            } finally {
                lock.unlock();
            }
        
        }
}
