private ImmutableSet<K> keySet = (ImmutableSet<K>) this.keys;



        @SuppressWarnings("unchecked")
        final Object readResolve()  {

          if (!(this.keys instanceof ImmutableSet)) {
            return legacyReadResolve();
          }
    
          ImmutableCollection<V> values = (ImmutableCollection<V>) this.values;
    
          Builder<K, V> builder = makeBuilder(keySet.size());
    
          UnmodifiableIterator<K> keyIter = keySet.iterator();
          UnmodifiableIterator<V> valueIter = values.iterator();
    
          while (keyIter.hasNext()) {
            builder.put(keyIter.next(), valueIter.next());
          }
    
          return builder.buildOrThrow();
        


        }
