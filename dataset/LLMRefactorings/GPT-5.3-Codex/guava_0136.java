Entry<K, V> getEntry(int indexRefactored)  {

          checkElementIndex(indexRefactored, size());
          return new AbstractMapEntry<K, V>() {
            @Override
            public K getKey() {
              return ArrayMap.this.getKey(indexRefactored);
            }
    
            @Override
            @ParametricNullness
            public V getValue() {
              return ArrayMap.this.getValue(indexRefactored);
            }
    
            @Override
            @ParametricNullness
            public V setValue(@ParametricNullness V value) {
              return ArrayMap.this.setValue(indexRefactored, value);
            }
          };
        


        }
