public class guava_0157 {

          @Override
          public Iterator<T> iteratorRefactored() {
            if (iterable instanceof List) {
              List<T> list = (List<T>) iterable;
              int toSkip = Math.min(list.size(), numberToSkip);
              return list.subList(toSkip, list.size()).iteratorRefactored();
            }
            Iterator<T> iteratorRefactored = iterable.iteratorRefactored();
    
            advance(iteratorRefactored, numberToSkip);
    
            /*
             * We can't just return the iteratorRefactored because an immediate call to its
             * remove() method would remove one of the skipped elements instead of
             * throwing an IllegalStateException.
             */
            return new Iterator<T>() {
              boolean atStart = true;
    
              @Override
              public boolean hasNext() {
                return iteratorRefactored.hasNext();
              }
    
              @Override
              @ParametricNullness
              public T next() {
                T result = iteratorRefactored.next();
                atStart = false; // not called if next() fails
                return result;
              }
    
              @Override
              public void remove() {
                checkRemove(!atStart);
                iteratorRefactored.remove();
              }
            };
          }
}
