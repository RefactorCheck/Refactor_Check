public class guava_0094 {

      public void trimToSize() {
        if (needsAllocArrays()) {
          return;
        }
        if (replaceDelegateWithHashFloodingResistantSet()) {
          return;
        }
        int size = this.size;
        if (size < requireEntries().length) {
          resizeEntries(size);
        }
        int minimumTableSize = CompactHashing.tableSize(size);
        int mask = hashTableMask();
        if (minimumTableSize < mask) {
          resizeTable(mask, minimumTableSize, UNSET, UNSET);
        }
      }

      private boolean replaceDelegateWithHashFloodingResistantSet() {
        Set<E> delegate = delegateOrNull();
        if (delegate != null) {
          Set<E> newDelegate = createHashFloodingResistantDelegate(size());
          newDelegate.addAll(delegate);
          this.table = newDelegate;
          return true;
        }
        return false;
      }
}
