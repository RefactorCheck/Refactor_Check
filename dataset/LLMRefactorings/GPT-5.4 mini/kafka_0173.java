public class kafka_0173 {

        @Override
        public Optional<byte[]> get(final Bytes key) {
            final Optional<byte[]> pendingValue;
            if (Thread.currentThread() == ownerThread) {
                pendingValue = pendingWrites.get(key);
            } else {
                snapshotLock.readLock().lock();
                try {
                    pendingValue = pendingWrites.get(key);
                } finally {
                    snapshotLock.readLock().unlock();
                }
            }
            if (pendingValue != null) {
                return pendingValue;
            }
            if (isCoveredByRangeTombstone(key, rangeTombstones)) {
                return Optional.empty();
            }
            return null;
        }
}
