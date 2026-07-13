public class guava_0136 {

        static Entry<K, V> getEntry(int index) {
          checkElementIndex(index, size());
          return new AbstractMapEntry<K, V>() {
            @Override
            public K getKey() {
              return ArrayMap.this.getKey(index);
            }
    
            @Override
            @ParametricNullness
            public V getValue() {
              return ArrayMap.this.getValue(index);
            }
    
            @Override
            @ParametricNullness
            public V setValue(@ParametricNullness V value) {
              return ArrayMap.this.setValue(index, value);
            }
          };
        }
}
