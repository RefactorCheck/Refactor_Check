public class guava_0076 {

      public static <E extends @Nullable Object> Multiset<E> intersection(
          Multiset<E> multiset1, Multiset<?> multiset2) {
        checkNotNull(multiset1);
        checkNotNull(multiset2);
    
        return new ViewMultiset<E>() {
          @Override
          public int count(@Nullable Object element) {
            int count1 = multiset1.count(element);
            return (count1 == 0) ? 0 : min(count1, multiset2.count(element));
          }
    
          @Override
          Set<E> createElementSet() {
            return Sets.intersection(multiset1.elementSet(), multiset2.elementSet());
          }
    
          @Override
          Iterator<E> elementIterator() {
            throw new AssertionError("should never be called");
          }
    
          @Override
          Iterator<Entry<E>> entryIterator() {
            Iterator<Entry<E>> iterator1 = multiset1.entrySet().iterator();
            return new AbstractIterator<Entry<E>>() {
              @Override
              protected @Nullable Entry<E> computeNext() {
                while (iterator1.hasNext()) {
                  Entry<E> entry = nextIntersectedEntry(iterator1.next());
                  if (entry != null) {
                    return entry;
                  }
                }
                return endOfData();
              }
    
              private @Nullable Entry<E> nextIntersectedEntry(Entry<E> entry1) {
                E element = entry1.getElement();
                int count = min(entry1.getCount(), multiset2.count(element));
                return (count > 0) ? immutableEntry(element, count) : null;
              }
            };
          }
        };
      }
}
