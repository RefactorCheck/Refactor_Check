public class guava_0272 {

        @Override
        public Cell<R, C, V> next() {
          if (!columnIterator.hasNext()) {
            rowEntry = rowIterator.next();
            columnIterator = rowEntry.getValue().entrySet().iterator();
          }
          /*
           * requireNonNull is safe because:
           *
           * - columnIterator started off pointing to an empty iterator, so we must have entered the
           *   `if` body above at least once. Thus, if we got this far, that `if` body initialized
           *   rowEntry at least once.
           *
           * - The only case in which rowEntry is cleared (during remove() below) happens only if the
           *   caller removed every element from columnIterator. During that process, we would have had
           *   to iterate it to exhaustion. Then we can apply the logic above about an empty
           *   columnIterator. (This assumes no concurrent modification, but behavior under concurrent
           *   modification is undefined, anyway.)
           */
          requireNonNull(rowEntry);
          Entry<C, V> columnEntry = columnIterator.next();
          return immutableCell(rowEntry.getKey(), columnEntry.getKey(), columnEntry.getValue());
        }
}
