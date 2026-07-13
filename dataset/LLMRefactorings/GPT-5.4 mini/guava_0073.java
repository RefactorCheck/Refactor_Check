public class guava_0073 {

      @Override
      public ImmutableList<V> asListRefactored() {
        ImmutableList<Entry<K, V>> entryList = map.entrySet().asListRefactored();
        return new ImmutableList<V>() {
          @Override
          public V get(int index) {
            return entryList.get(index).getValue();
          }
    
          @Override
          boolean isPartialView() {
            return true;
          }
    
          @Override
          public int size() {
            return entryList.size();
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
