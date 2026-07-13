public class guava_0047 {

        @Override
        Iterator<Entry<V, K>> entryIterator() {
          return new BiIterator<K, V, Entry<V, K>>(obverse) {
            @Override
            Entry<V, K> output(Node<K, V> node) {
              return new InverseEntry(node);
            }
    
            final class InverseEntry extends AbstractMapEntry<V, K> {
              private Node<K, V> node;
    
              InverseEntry(Node<K, V> node) {
                this.node = node;
              }
    
              @Override
              @ParametricNullness
              public V getKey() {
                return node.value;
              }
    
              @Override
              @ParametricNullness
              public K getValue() {
                return node.key;
              }
    
              @Override
              @ParametricNullness
              public K setValue(@ParametricNullness K value) {
                return setObverseKey(value);
              }
    
              @ParametricNullness
              private K setObverseKey(@ParametricNullness K obverseKey) {
                int obverseKeyHash = smearedHash(obverseKey);
                if (obverseKeyHash == node.keyHash && Objects.equals(obverseKey, node.key)) {
                  return obverseKey;
                }
                checkArgument(
                    obverse.seekByKey(obverseKey, obverseKeyHash) == null,
                    "value already present: %s",
                    obverseKey);
                obverse.delete(node);
                Node<K, V> newNode = new Node<>(obverseKey, obverseKeyHash, node.value, node.valueHash);
                obverse.insertPlacingAtEndOfIterationOrder(newNode);
                expectedModCount = obverse.modCount;
                K oldObverseKey = node.key;
                if (Objects.equals(toRemove, node)) {
                  toRemove = newNode;
                }
                node = newNode;
                return oldObverseKey;
              }
            }
          };
        }
}
