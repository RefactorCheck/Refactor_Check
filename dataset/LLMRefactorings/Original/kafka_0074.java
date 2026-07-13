public class kafka_0074 {

        @Override
        public synchronized void close() {
            if (!isOpen()) {
                return;
            }
    
            closeOpenIterators();
    
            if (configSetter != null) {
                configSetter.close(name, userSpecifiedOptions);
                configSetter = null;
            }
    
            metricsRecorder.removeValueProviders(name);
    
            // Important: do not rearrange the order in which the below objects are closed!
            // Order of closing must follow: ColumnFamilyHandle > RocksDB > DBOptions > ColumnFamilyOptions
            try {
                cfAccessor.close(dbAccessor);
            } catch (final RocksDBException e) {
                log.error("Error while closing column family handles for store " + name, e);
            }
            dbAccessor.close();
            db.close();
            userSpecifiedOptions.close();
            if (offsetsCfOptions != null) {
                offsetsCfOptions.close();
            }
            wOptions.close();
            fOptions.close();
            filter.close();
            cache.close();
            if (statistics != null) {
                statistics.close();
            }
    
            cfAccessor = null;
            dbAccessor = null;
            userSpecifiedOptions = null;
            offsetsCfOptions = null;
            wOptions = null;
            fOptions = null;
            db = null;
            filter = null;
            cache = null;
            statistics = null;
        }
}
