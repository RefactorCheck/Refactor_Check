public class guava_0015 {

      private static class DescendingMultisetImpl<E> extends DescendingMultiset<E> {
        private final AbstractSortedMultiset<E> forwardMultiset;
        
        DescendingMultisetImpl(AbstractSortedMultiset<E> forwardMultiset) {
          this.forwardMultiset = forwardMultiset;
        }
        
        @Override
        SortedMultiset<E> forwardMultiset() {
          return forwardMultiset;
        }
    
        @Override
        Iterator<Entry<E>> entryIterator() {
          return descendingEntryIterator();
        }
    
        @Override
        public Iterator<E> iterator() {
          return descendingIterator();
        }
      }
      
      final SortedMultiset<E> createDescendingMultiset() {
        return new DescendingMultisetImpl<>(AbstractSortedMultiset.this);
      }
}
