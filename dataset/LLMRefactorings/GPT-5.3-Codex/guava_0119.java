private AvlNode<E> next = current.succ();



      @Override
      public void clear()  {

        if (!range.hasLowerBound() && !range.hasUpperBound()) {
          // We can do this in O(n) rather than removing one by one, which could force rebalancing.
          for (AvlNode<E> current = header.succ(); current != header; ) {
    
            current.elemCount = 0;
            // Also clear these fields so that one deleted Entry doesn't retain all elements.
            current.left = null;
            current.right = null;
            current.pred = null;
            current.succ = null;
    
            current = next;
          }
          successor(header, header);
          rootReference.clear();
        } else {
          // TODO(cpovirk): Perhaps we can optimize in this case, too?
          Iterators.clear(entryIterator());
        }
      


      }
