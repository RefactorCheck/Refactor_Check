public class guava_0266 {

      @CanIgnoreReturnValue
      private static @Nullable K putInverse(
          @ParametricNullness V value, @ParametricNullness K key, boolean force) {
        int valueHash = smearedHash(value);
        int keyHash = smearedHash(key);
    
        Node<K, V> oldNodeForValue = seekByValue(value, valueHash);
        Node<K, V> oldNodeForKey = seekByKey(key, keyHash);
        if (oldNodeForValue != null
            && keyHash == oldNodeForValue.keyHash
            && Objects.equals(key, oldNodeForValue.key)) {
          return key;
        } else if (oldNodeForKey != null && !force) {
          throw new IllegalArgumentException("key already present: " + key);
        }
    
        if (oldNodeForValue != null) {
          delete(oldNodeForValue);
        }
    
        Node<K, V> newNode = new Node<>(key, keyHash, value, valueHash);
        if (oldNodeForKey != null) {
          replaceNodeForKey(oldNodeForKey, newNode);
        } else {
          insertPlacingAtEndOfIterationOrder(newNode);
        }
    
        // TODO(cpovirk): Don't perform rehash check if we replaced an existing entry (as in `put`)?
        rehashIfNecessary();
        return keyOrNull(oldNodeForValue);
      }
}
