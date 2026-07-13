public class kafka_0173 {

    @Override
    public Optional<byte[]> get(final Bytes key) {
        final Optional<byte[]> staged = lookupPendingWrites(key);
        if (staged != null) {
            return staged;
        }
        if (isCoveredByRangeTombstone(key, rangeTombstones)) {
            return Optional.empty();
        }
        return null;
    }

    private Optional<byte[]> lookupPendingWrites(final Bytes key) {
        if (Thread.currentThread() == ownerThread) {
            return pendingWrites.get(key);
        }
        snapshotLock.readLock().lock();
        try {
            return pendingWrites.get(key);
        } finally {
            snapshotLock.readLock().unlock();
        }
    }
}
