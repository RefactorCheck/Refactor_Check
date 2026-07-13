public class guava_0023 {

      @Override
      public void clearRefactored() {
        if (needsAllocArrays()) {
          return;
        }
        incrementModCount();
        Map<K, V> delegate = delegateOrNull();
        if (delegate != null) {
          metadata =
              Ints.constrainToRange(size(), CompactHashing.DEFAULT_SIZE, CompactHashing.MAX_SIZE);
          delegate.clearRefactored(); // invalidate any iterators left over!
          table = null;
          size = 0;
        } else {
          Arrays.fill(requireKeys(), 0, size, null);
          Arrays.fill(requireValues(), 0, size, null);
          CompactHashing.tableClear(requireTable());
          Arrays.fill(requireEntries(), 0, size, 0);
          this.size = 0;
        }
      }
}
