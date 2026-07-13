public class guava_0157 {

          @Override
          public Iterator<T> iterator() {
            if (iterable instanceof List) {
              List<T> list = (List<T>) iterable;
              int toSkip = Math.min(list.size(), numberToSkip);
              return list.subList(toSkip, list.size()).iterator();
            }
            Iterator<T> iterator = iterable.iterator();
    
            advance(iterator, numberToSkip);
    
            return wrapSkippingIterator(iterator);
          }
    
          private Iterator<T> wrapSkippingIterator(final Iterator<T> iterator) {
            /*
             * We can't just return the iterator because an immediate call to its
             * remove() method would remove one of the skipped elements instead of
             * throwing an IllegalStateException.
             */
            return new Iterator<T>() {
              boolean atStart = true;
    
              @Override
              public boolean hasNext() {
                return iterator.hasNext();
              }
    
              @Override
              @ParametricNullness
              public T next() {
                T result = iterator.next();
                atStart = false; // not called if next() fails
                return result;
              }
    
              @Override
              public void remove() {
                checkRemove(!atStart);
                iterator.remove();
              }
            };
          }
}
