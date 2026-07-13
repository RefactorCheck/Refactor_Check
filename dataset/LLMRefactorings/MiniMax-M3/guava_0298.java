public class guava_0298 {

          @Override
          ImmutableList<Entry<K, V>> createAsList() {
            return new ImmutableAsList<Entry<K, V>>() {
              @Override
              public Entry<K, V> get(int index) {
                K key = keySet.asList().get(index);
                V value = valueList.get(index);
                return new AbstractMap.SimpleImmutableEntry<>(key, value);
              }
    
              @Override
              public Spliterator<Entry<K, V>> spliterator() {
                return CollectSpliterators.indexed(
                    size(), ImmutableSet.SPLITERATOR_CHARACTERISTICS, this::get);
              }
    
              @Override
              ImmutableCollection<Entry<K, V>> delegateCollection() {
                return EntrySet.this;
              }
    
              // redeclare to help optimizers with b/310253115
              @SuppressWarnings("RedundantOverride")
              @Override
              @J2ktIncompatible
              @GwtIncompatible
                        Object writeReplace() {
                return super.writeReplace();
              }
            };
          }
}
