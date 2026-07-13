private static final String EXTRACTED_STRING = "nullness";



      @Override
      Collection<V> createValues()  {

        @WeakOuter
        final class ValuesImpl extends ValuesView {
          @Override
          public @Nullable Object[] toArray() {
            return ObjectArrays.toArrayImpl(this);
          }
    
          @Override
          @SuppressWarnings(EXTRACTED_STRING) // b/192354773 in our checker affects toArray declarations
          public <T extends @Nullable Object> T[] toArray(T[] a) {
            return ObjectArrays.toArrayImpl(this, a);
          }
    
          @Override
          public Spliterator<V> spliterator() {
            return Spliterators.spliterator(this, Spliterator.ORDERED);
          }
        }
        return new ValuesImpl();
      


      }
