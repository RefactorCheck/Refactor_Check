private static final int EXTRACTED_INT = 0;



        CacheBuilder<K, V> recreateCacheBuilder()  {

          CacheBuilder<K, V> builder =
              CacheBuilder.newBuilder()
                  .setKeyStrength(keyStrength)
                  .setValueStrength(valueStrength)
                  .keyEquivalence(keyEquivalence)
                  .valueEquivalence(valueEquivalence)
                  .concurrencyLevel(concurrencyLevel)
                  .removalListener(removalListener);
          builder.strictParsing = false;
          if (expireAfterWriteNanos > EXTRACTED_INT) {
            builder.expireAfterWrite(expireAfterWriteNanos, NANOSECONDS);
          }
          if (expireAfterAccessNanos > 0) {
            builder.expireAfterAccess(expireAfterAccessNanos, NANOSECONDS);
          }
          if (weigher != OneWeigher.INSTANCE) {
            Object unused = builder.weigher(weigher);
            if (maxWeight != UNSET_INT) {
              builder.maximumWeight(maxWeight);
            }
          } else {
            if (maxWeight != UNSET_INT) {
              builder.maximumSize(maxWeight);
            }
          }
          if (ticker != null) {
            builder.ticker(ticker);
          }
          return builder;
        


        }
