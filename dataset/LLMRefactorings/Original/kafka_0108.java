public class kafka_0108 {

        void stageDeleteRange(final ColumnFamilyHandle cf, final Bytes from, final Bytes to) {
            snapshotLock.writeLock().lock();
            try {
                // Multi-node subMap().clear() on the (non-thread-safe) TreeMap staging buffer must hold
                // the write lock; doing it together with the rangeTombstones swap also makes the two
                // updates atomic with respect to a non-owner snapshot scan.
                pendingWrites.subMap(from, true, to, false).clear();
                // Copy-on-write: clone the map AND the affected key's list, so a non-owner iterator
                // holding the previous rangeTombstones map (and its lists) is never mutated underneath
                // it. A shallow map copy alone would share the List<Bytes> values and corrupt in-flight
                // iterators when the same `from` is deleted twice.
                final TreeMap<Bytes, List<Bytes>> copy = new TreeMap<>(rangeTombstones);
                final List<Bytes> existing = copy.get(from);
                final List<Bytes> updated = existing == null ? new ArrayList<>() : new ArrayList<>(existing);
                updated.add(to);
                copy.put(from, updated);
                rangeTombstones = copy;
                pendingWritesBytes += estimateKeySize(from) + estimateKeySize(to);
                try {
                    writeBatch.deleteRange(cf, from.get(), to.get());
                } catch (final RocksDBException e) {
                    throw new ProcessorStateException("Error staging delete range in transaction buffer for store " + storeName, e);
                }
            } finally {
                snapshotLock.writeLock().unlock();
            }
        }
}
