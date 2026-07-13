public class guava_0138 {

      @Override
      public boolean containsAll(Collection<?> targets) {
        // TODO(jlevy): For optimal performance, use a binary search when
        // targets.size() < size() / log(size())
        // TODO(kevinb): see if we can share code with OrderedIterator after it
        // graduates from labs.
        if (targets instanceof Multiset) {
          targets = ((Multiset<?>) targets).elementSet();
        }
        if (!SortedIterables.hasSameComparator(comparator(), targets) || (targets.size() <= 1)) {
          return super.containsAll(targets);
        }
    
        /*
         * If targets is a sorted set with the same comparator, containsAll can run
         * in O(n) time stepping through the two collections.
         */
        Iterator<E> thisIterator = iterator();
    
        Iterator<?> thatIterator = targets.iterator();
        // known nonempty since we checked targets.size() > 1
    
        if (!thisIterator.hasNext()) {
          return false;
        }
    
        Object target = thatIterator.next();
        E current = thisIterator.next();
        try {
          while (true) {
            int cmp = unsafeCompare(current, target);
    
            if (cmp < 0) {
              if (!thisIterator.hasNext()) {
                return false;
              }
              current = thisIterator.next();
            } else if (cmp == 0) {
              if (!thatIterator.hasNext()) {
                return true;
              }
              target = thatIterator.next();
    
            } else if (cmp > 0) {
              return false;
            }
          }
        } catch (NullPointerException | ClassCastException e) {
          return false;
        }
      }
}
