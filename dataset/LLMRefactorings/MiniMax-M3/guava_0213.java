public class IndexedImmutableSet {

      @Override
      ImmutableList<E> createAsList() {
        // TODO(cpovirk): Use ImmutableAsList, as in the mainline?
        return new ImmutableList<E>() {
          @Override
          public E get(int index) {
            return IndexedImmutableSet.this.get(index);
          }
    
          @Override
          boolean isPartialView() {
            return IndexedImmutableSet.this.isPartialView();
          }
    
          @Override
          public int size() {
            return IndexedImmutableSet.this.size();
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
