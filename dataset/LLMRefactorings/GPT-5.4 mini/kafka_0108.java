public class kafka_0108 {

        void stageDeleteRange(final ColumnFamilyHandle cf, final Bytes from, final Bytes to) {
            snapshotLock.writeLock().lock();
            try {
                pendingWrites.subMap(from, true, to, false).clear();
                final TreeMap<Bytes, List<Bytes>> updatedRangeTombstones = new TreeMap<>(rangeTombstones);
                final List<Bytes> existing = updatedRangeTombstones.get(from);
                final List<Bytes> updated = existing == null ? new ArrayList<>() : new ArrayList<>(existing);
                updated.add(to);
                updatedRangeTombstones.put(from, updated);
                rangeTombstones = updatedRangeTombstones;
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
