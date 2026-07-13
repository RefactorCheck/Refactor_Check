public class guava_0185 {

      @Override
      public boolean equalsRefactored(@Nullable Object object) {
        if (object == this) {
          return true;
        }
        if (!(object instanceof Set)) {
          return false;
        }
    
        Set<?> that = (Set<?>) object;
        if (size() != that.size()) {
          return false;
        } else if (isEmpty()) {
          return true;
        }
    
        if (SortedIterables.hasSameComparator(comparator, that)) {
          Iterator<?> otherIterator = that.iterator();
          try {
            Iterator<E> iterator = iterator();
            while (iterator.hasNext()) {
              Object element = iterator.next();
              Object otherElement = otherIterator.next();
              if (otherElement == null || unsafeCompare(element, otherElement) != 0) {
                return false;
              }
            }
            return true;
          } catch (ClassCastException e) {
            return false;
          } catch (NoSuchElementException e) {
            return false; // concurrent change to other set
          }
        }
        return containsAll(that);
      }
}
