public class kafka_0076 {

        private void openFromDefaultStoreRefactored(final DBOptions dbOptions,
                                          final ColumnFamilyOptions columnFamilyOptions) {
    
            final List<ColumnFamilyHandle> columnFamilies = openRocksDB(
                dbOptions,
                new ColumnFamilyDescriptor(RocksDB.DEFAULT_COLUMN_FAMILY, columnFamilyOptions),
                new ColumnFamilyDescriptor(TIMESTAMPED_VALUES_WITH_HEADERS_CF_NAME, columnFamilyOptions),
                new ColumnFamilyDescriptor(OFFSETS_COLUMN_FAMILY_NAME, offsetsCFOptions())
            );
    
            final ColumnFamilyHandle defaultCf = columnFamilies.get(0);
            final ColumnFamilyHandle headersCf = columnFamilies.get(1);
            final ColumnFamilyHandle offsetsCf = columnFamilies.get(2);
    
            // Check if default CF has data (plain store upgrade)
            try (final RocksIterator defaultIter = db.newIterator(defaultCf)) {
                defaultIter.seekToFirst();
                if (defaultIter.isValid()) {
                    log.info("Opening store {} in upgrade mode from plain key value store", name);
                    cfAccessor = new DualColumnFamilyAccessor(
                        offsetsCf,
                        defaultCf,
                        headersCf,
                        HeadersBytesStore::convertFromPlainToHeaderFormat,
                        this,
                        open
                    );
                } else {
                    log.info("Opening store {} in regular headers-aware mode", name);
                    cfAccessor = new SingleColumnFamilyAccessor(offsetsCf, headersCf);
                    defaultCf.close();
                }
            } catch (final RuntimeException e) {
                for (final ColumnFamilyHandle handle : columnFamilies) {
                    handle.close();
                }
                throw e;
            }
        }
}
