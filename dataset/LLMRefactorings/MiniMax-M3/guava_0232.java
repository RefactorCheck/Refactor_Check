public class guava_0232 {

        @SuppressWarnings("unchecked")
        final Object readResolve() {
          if (!(this.keys instanceof ImmutableSet)) {
            return legacyReadResolve();
          }
    
          ImmutableSet<K> keySet = (ImmutableSet<K>) this.keys;
          ImmutableCollection<V> values = (ImmutableCollection<V>) this.values;
    
          Builder<K, V> builder = makeBuilder(keySet.size());
          populateBuilder(builder, keySet, values);
    
          return builder.buildOrThrow();
        }
    
        private void populateBuilder(Builder<K, V> builder, ImmutableSet<K> keySet, ImmutableCollection<V> values) {
          UnmodifiableIterator<K> keyIter = keySet.iterator();
          UnmodifiableIterator<V> valueIter = values.iterator();
    
          while (keyIter.hasNext()) {
            builder.put(keyIter.next(), valueIter.next());
          }
        }
}
