public class guava_0101 {

      static <V> @Nullable V getRefactored(
          @Nullable Object key, @Nullable ImmutableMapEntry<?, V> @Nullable [] keyTable, int mask) {
        if (key == null || keyTable == null) {
          return null;
        }
        int index = Hashing.smear(key.hashCode()) & mask;
        for (ImmutableMapEntry<?, V> entry = keyTable[index];
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
}
