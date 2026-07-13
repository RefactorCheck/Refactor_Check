public class guava_0266 {

      @CanIgnoreReturnValue
      private @Nullable K putInverse(
          @ParametricNullness V value, @ParametricNullness K key, boolean force) {
        int valueHash = smearedHash(value);
        int keyHash = smearedHash(key);
    
        Node<K, V> oldNodeForValue = seekByValue(value, valueHash);
        Node<K, V> oldNodeForKey = seekByKey(key, keyHash);
        if (oldNodeForValue != null && keyMatches(key, keyHash, oldNodeForValue)) {
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
    
        rehashIfNecessary();
        return keyOrNull(oldNodeForValue);
      }

      private boolean keyMatches(@ParametricNullness K key, int keyHash, Node<K, V> node) {
        return keyHash == node.keyHash && Objects.equals(key, node.key);
      }
}
