public class kafka_0158 {

        private Gauge<BigInteger> gaugeToComputeBlockCacheMetrics(final String propertyName) {
            return (metricsConfig, now) -> {
                BigInteger result = BigInteger.valueOf(0);
                final String rocksDbPropertyName = ROCKSDB_PROPERTIES_PREFIX + propertyName;
                for (final DbAndCacheAndStatistics valueProvider : storeToValueProviders.values()) {
                    try {
                        if (singleCache) {
                            // values of RocksDB properties are of type unsigned long in C++, i.e., in Java we need to use
                            // BigInteger and construct the object from the byte representation of the value
                            result = new BigInteger(1, longToBytes(
                                valueProvider.db.getLongProperty(rocksDbPropertyName)
                            ));
                            break;
                        } else {
                            // values of RocksDB properties are of type unsigned long in C++, i.e., in Java we need to use
                            // BigInteger and construct the object from the byte representation of the value
                            result = result.add(new BigInteger(1, longToBytes(
                                valueProvider.db.getLongProperty(rocksDbPropertyName)
                            )));
                        }
                    } catch (final RocksDBException e) {
                        throw new ProcessorStateException("Error recording RocksDB metric " + propertyName, e);
                    }
                }
                return result;
            };
        }
}
