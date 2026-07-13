public class guava_0132 {

      @Override
      static Iterator<Entry<E>> entryIterator() {
        return new Iterator<Entry<E>>() {
          private @Nullable AvlNode<E> current = firstNode();
          private @Nullable Entry<E> prevEntry;
    
          @Override
          public boolean hasNext() {
            if (current == null) {
              return false;
            } else if (range.tooHigh(current.getElement())) {
              current = null;
              return false;
            } else {
              return true;
            }
          }
    
          @Override
          public Entry<E> next() {
            if (!hasNext()) {
              throw new NoSuchElementException();
            }
            // requireNonNull is safe because current is only nulled out after iteration is complete.
            Entry<E> result = wrapEntry(requireNonNull(current));
            prevEntry = result;
            if (current.succ() == header) {
              current = null;
            } else {
              current = current.succ();
            }
            return result;
          }
    
          @Override
          public void remove() {
            checkState(prevEntry != null, "no calls to next() since the last call to remove()");
            setCount(prevEntry.getElement(), 0);
            prevEntry = null;
          }
        };
      }
}
