static <V> @Nullable V get(
          @Nullable Object key, @Nullable ImmutableMapEntry<?, V> @Nullable [] keyTable, int mask)  {

        if (key == null || keyTable == null) {
          return null;
        }
        for (ImmutableMapEntry<?, V> entry = keyTable[Hashing.smear(key.hashCode()) & mask];
            entry != null;
            entry = entry.getNextInKeyBucket()) {
          Object candidateKey = entry.getKey();
    
          /*
           * Assume that equals uses the == optimization when appropriate, and that
           * it would check hash codes as an optimization when appropriate. If we
           * did these things, it would just make things worse for the most
           * performance-conscious users.
           */
          if (key.equals(candidateKey)) {
            return entry.getValue();
          }
        }
        return null;
      


      }
