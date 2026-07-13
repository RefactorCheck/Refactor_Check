public class guava_0192 {

      @Override
      public void clear() {
        if (needsAllocArrays()) {
          return;
        }
        incrementModCount();
        Set<E> delegate = delegateOrNull();
        if (delegate != null) {
          clearWithDelegate(delegate);
        } else {
          clearArrays();
        }
      }

      private void clearWithDelegate(Set<E> delegate) {
        metadata =
            Ints.constrainToRange(size(), CompactHashing.DEFAULT_SIZE, CompactHashing.MAX_SIZE);
        delegate.clear();
        table = null;
        size = 0;
      }

      private void clearArrays() {
        Arrays.fill(requireElements(), 0, size, null);
        CompactHashing.tableClear(requireTable());
        Arrays.fill(requireEntries(), 0, size, 0);
        this.size = 0;
      }
}
