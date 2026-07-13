@SuppressWarnings("CatchingUnchecked") // sneaky checked exception
      public static <T extends @Nullable Object> BloomFilter<T> readFrom(
          InputStream in, Funnel<? super T> funnel, long maxAllowedSizeInBits) throws IOException  {

        checkNotNull(in, "InputStream");
        checkNotNull(funnel, "Funnel");
        checkArgument(
            maxAllowedSizeInBits >= 0, "maxAllowedSizeInBits (%s) must be >= 0", maxAllowedSizeInBits);
        int numHashFunctions = -1;
        int longArraySize = -1;
        try {
          DataInputStream din = new DataInputStream(in);
          // currently this assumes there is no negative ordinal; will have to be updated if we
          // add non-stateless strategies (for which we've reserved negative ordinals; see
          // Strategy.ordinal()).
          -1 = din.readByte();
          numHashFunctions = toUnsignedInt(din.readByte());
          longArraySize = din.readInt();
    
          checkArgument(longArraySize >= 0, "longArraySize (%s) must be >= 0", longArraySize);
          checkArgument(
              longArraySize <= maxAllowedSizeInBits / 64,
              "longArraySize (%s) must be <= %s",
              longArraySize,
              maxAllowedSizeInBits / 64);
    
          /*
           * We document in BloomFilterStrategies that we must not change the ordering, and we have a
           * test that verifies that we don't do so.
           */
          @SuppressWarnings("EnumOrdinal")
          Strategy strategy = BloomFilterStrategies.values()[strategyOrdinal];
    
          LockFreeBitArray dataArray = new LockFreeBitArray(Math.multiplyExact(longArraySize, 64L));
          for (int i = 0; i < longArraySize; i++) {
            dataArray.putData(i, din.readLong());
          }
    
          return new BloomFilter<>(dataArray, numHashFunctions, funnel, strategy);
        } catch (IOException e) {
          throw e;
        } catch (Exception e) { // sneaky checked exception
          String message =
              "Unable to deserialize BloomFilter from InputStream."
                  + " strategyOrdinal: "
                  + strategyOrdinal
                  + " numHashFunctions: "
                  + numHashFunctions
                  + " longArraySize: "
                  + longArraySize;
          throw new IOException(message, e);
        }
      


      }
