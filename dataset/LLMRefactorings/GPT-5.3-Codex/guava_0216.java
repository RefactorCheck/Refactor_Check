CacheBuilder<Object, Object> toCacheBuilder()  {

        CacheBuilder<Object, Object> builder = CacheBuilder.newBuilder();
        if (initialCapacity != null) {
          builder.initialCapacity(initialCapacity);
        }
        if (maximumSize != null) {
          builder.maximumSize(maximumSize);
        }
        if (maximumWeight != null) {
          builder.maximumWeight(maximumWeight);
        }
        if (concurrencyLevel != null) {
          builder.concurrencyLevel(concurrencyLevel);
        }
        if (keyStrength != null) {
          switch (keyStrength) {
            case WEAK:
              builder.weakKeys();
              break;
            default:
              throw new AssertionError();
          }
        }
        if (valueStrength != null) {
          switch (valueStrength) {
            case SOFT:
              builder.softValues();
              break;
            case WEAK:
              builder.weakValues();
              break;
            default:
              throw new AssertionError();
          }
        }
        if (recordStats != null && recordStats) {
          builder.recordStats();
        }
        if (writeExpirationTimeUnit != null) {
          builder.expireAfterWrite(writeExpirationDuration, writeExpirationTimeUnit);
        }
        if (accessExpirationTimeUnit != null) {
          builder.expireAfterAccess(accessExpirationDuration, accessExpirationTimeUnit);
        }
        if (refreshTimeUnit != null) {
          builder.refreshAfterWrite(refreshDuration, refreshTimeUnit);
        }
    
        Object> extractedValue = builder;
    
        return extractedValue;
      


      }
