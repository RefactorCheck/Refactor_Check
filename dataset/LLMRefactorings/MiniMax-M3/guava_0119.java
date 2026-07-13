public class guava_0119 {

      @Override
      public void clear() {
        if (!range.hasLowerBound() && !range.hasUpperBound()) {
          // We can do this in O(n) rather than removing one by one, which could force rebalancing.
          for (AvlNode<E> current = header.succ(); current != header; ) {
            AvlNode<E> next = current.succ();
    
            clearAvlNode(current);
    
            current = next;
          }
          successor(header, header);
          rootReference.clear();
        } else {
          // TODO(cpovirk): Perhaps we can optimize in this case, too?
          Iterators.clear(entryIterator());
        }
      }

      private void clearAvlNode(AvlNode<E> node) {
        node.elemCount = 0;
        // Also clear these fields so that one deleted Entry doesn't retain all elements.
        node.left = null;
        node.right = null;
        node.pred = null;
        node.succ = null;
      }
}
