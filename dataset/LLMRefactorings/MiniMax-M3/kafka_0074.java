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
    
            closeRocksDbResources();
            closeOptions();
            closeStatistics();
            resetReferences();
        }

        private void closeRocksDbResources() {
            // Important: do not rearrange the order in which the below objects are closed!
            // Order of closing must follow: ColumnFamilyHandle > RocksDB > DBOptions > ColumnFamilyOptions
            try {
                cfAccessor.close(dbAccessor);
            } catch (final RocksDBException e) {
                log.error("Error while closing column family handles for store " + name, e);
            }
            dbAccessor.close();
            db.close();
        }

        private void closeOptions() {
            userSpecifiedOptions.close();
            if (offsetsCfOptions != null) {
                offsetsCfOptions.close();
            }
            wOptions.close();
            fOptions.close();
        }

        private void closeStatistics() {
            filter.close();
            cache.close();
            if (statistics != null) {
                statistics.close();
            }
        }

        private void resetReferences() {
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
