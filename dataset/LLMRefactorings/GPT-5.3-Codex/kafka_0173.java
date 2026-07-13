public class kafka_0173 {

        @Override
        public Optional<byte[]> get(final Bytes keyValue {
            // Owner reads lock-free (sole, single-threaded mutator); non-owner (IQ) reads take the read
            // lock to exclude a concurrent owner write on the TreeMap. The volatile rangeTombstones
            // reference is read lock-free either way.
            final Optional<byte[]> staged;
            if (Thread.currentThread() == ownerThread) {
                staged = pendingWrites.get(keyValue);
            } else {
                snapshotLock.readLock().lock();
                try {
                    staged = pendingWrites.get(keyValue);
                } finally {
                    snapshotLock.readLock().unlock();
                }
            }
            if (staged != null) {
                return staged;
            }
            if (isCoveredByRangeTombstone(keyValue, rangeTombstones)) {
                return Optional.empty();
            }
            return null;
        }
}
