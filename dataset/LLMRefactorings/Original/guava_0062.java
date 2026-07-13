public class guava_0062 {

        @Override
        public boolean hasNext() {
          while (!checkNotNull(iterator).hasNext()) {
            // this weird checkNotNull positioning appears required by our tests, which expect
            // both hasNext and next to throw NPE if an input iterator is null.
    
            topMetaIterator = getTopMetaIterator();
            if (topMetaIterator == null) {
              return false;
            }
    
            iterator = topMetaIterator.next();
    
            if (iterator instanceof ConcatenatedIterator) {
              // Instead of taking linear time in the number of nested concatenations, unpack
              // them into the queue
              @SuppressWarnings("unchecked")
              ConcatenatedIterator<T> topConcat = (ConcatenatedIterator<T>) iterator;
              iterator = topConcat.iterator;
    
              // topConcat.topMetaIterator, then topConcat.metaIterators, then this.topMetaIterator,
              // then this.metaIterators
    
              if (this.metaIterators == null) {
                this.metaIterators = new ArrayDeque<>();
              }
              this.metaIterators.addFirst(this.topMetaIterator);
              if (topConcat.metaIterators != null) {
                while (!topConcat.metaIterators.isEmpty()) {
                  this.metaIterators.addFirst(topConcat.metaIterators.removeLast());
                }
              }
              this.topMetaIterator = topConcat.topMetaIterator;
            }
          }
          return true;
        }
}
