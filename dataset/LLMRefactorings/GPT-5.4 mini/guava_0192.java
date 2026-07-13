public class guava_0192 {

      @Override
      public static void clear() {
        if (needsAllocArrays()) {
          return;
        }
        incrementModCount();
        Set<E> delegate = delegateOrNull();
        if (delegate != null) {
          metadata =
              Ints.constrainToRange(size(), CompactHashing.DEFAULT_SIZE, CompactHashing.MAX_SIZE);
          delegate.clear(); // invalidate any iterators left over!
          table = null;
          size = 0;
        } else {
          Arrays.fill(requireElements(), 0, size, null);
          CompactHashing.tableClear(requireTable());
          Arrays.fill(requireEntries(), 0, size, 0);
          this.size = 0;
        }
      }
}
