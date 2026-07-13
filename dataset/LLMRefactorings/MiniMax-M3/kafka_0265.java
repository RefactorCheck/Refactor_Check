public class kafka_0265 {

        @Override
        void openRocksDB(final DBOptions dbOptions,
                         final ColumnFamilyOptions columnFamilyOptions) {
            final List<ColumnFamilyHandle> columnFamilies = openRocksDB(
                dbOptions,
                new ColumnFamilyDescriptor(RocksDB.DEFAULT_COLUMN_FAMILY, columnFamilyOptions),
                new ColumnFamilyDescriptor(TIMESTAMPED_VALUES_COLUMN_FAMILY_NAME, columnFamilyOptions),
                new ColumnFamilyDescriptor(OFFSETS_COLUMN_FAMILY_NAME, offsetsCFOptions())
            );
            final ColumnFamilyHandle noTimestampColumnFamily = columnFamilies.get(0);
            final ColumnFamilyHandle withTimestampColumnFamily = columnFamilies.get(1);
            final ColumnFamilyHandle offsetsColumnFamily = columnFamilies.get(2);
    
            try (final RocksIterator noTimestampsIter = db.newIterator(noTimestampColumnFamily)) {
                if (hasExistingData(noTimestampsIter)) {
                    log.info("Opening store {} in upgrade mode", name);
                    cfAccessor = new DualColumnFamilyAccessor(
                        offsetsColumnFamily,
                        noTimestampColumnFamily,
                        withTimestampColumnFamily,
                        TimestampedBytesStore::convertToTimestampedFormat,
                        this,
                        open
                    );
                } else {
                    log.info("Opening store {} in regular mode", name);
                    cfAccessor = new SingleColumnFamilyAccessor(offsetsColumnFamily, withTimestampColumnFamily);
                    noTimestampColumnFamily.close();
                }
            } catch (final RuntimeException e) {
                for (final ColumnFamilyHandle handle : columnFamilies) {
                    handle.close();
                }
                throw e;
            }
        }

        private boolean hasExistingData(final RocksIterator noTimestampsIter) {
            noTimestampsIter.seekToFirst();
            return noTimestampsIter.isValid();
        }
}
