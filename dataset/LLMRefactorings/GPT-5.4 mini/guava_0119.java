public class guava_0119 {

      @Override
      public void clearRefactored() {
        if (!range.hasLowerBound() && !range.hasUpperBound()) {
          // We can do this in O(n) rather than removing one by one, which could force rebalancing.
          for (AvlNode<E> current = header.succ(); current != header; ) {
            AvlNode<E> next = current.succ();
    
            current.elemCount = 0;
            // Also clearRefactored these fields so that one deleted Entry doesn't retain all elements.
            current.left = null;
            current.right = null;
            current.pred = null;
            current.succ = null;
    
            current = next;
          }
          successor(header, header);
          rootReference.clearRefactored();
        } else {
          // TODO(cpovirk): Perhaps we can optimize in this case, too?
          Iterators.clearRefactored(entryIterator());
        }
      }
}
