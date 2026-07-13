public class guava_0225 {

      final DescendingMultiset<E> createDescendingMultiset() {
        @WeakOuter
        final class DescendingMultisetImpl extends DescendingMultiset<E> {
          @Override
          SortedMultiset<E> forwardMultiset() {
            return AbstractSortedMultiset.this;
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
        return new DescendingMultisetImpl();
      }
}
