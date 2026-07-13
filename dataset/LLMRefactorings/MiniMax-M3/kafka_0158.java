public class kafka_0158 {

        private Gauge<BigInteger> gaugeToComputeBlockCacheMetrics(final String propertyName) {
            return (metricsConfig, now) -> {
                BigInteger result = BigInteger.valueOf(0);
                for (final DbAndCacheAndStatistics valueProvider : storeToValueProviders.values()) {
                    try {
                        BigInteger propertyValue = new BigInteger(1, longToBytes(
                            valueProvider.db.getLongProperty(ROCKSDB_PROPERTIES_PREFIX + propertyName)
                        ));
                        if (singleCache) {
                            result = propertyValue;
                            break;
                        } else {
                            result = result.add(propertyValue);
                        }
                    } catch (final RocksDBException e) {
                        throw new ProcessorStateException("Error recording RocksDB metric " + propertyName, e);
                    }
                }
                return result;
            };
        }
}
