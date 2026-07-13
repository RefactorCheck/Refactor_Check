public class guava_0293 {

        @Override
        public L getAt(int index) {
          if (size != Integer.MAX_VALUE) {
            Preconditions.checkElementIndex(index, size());
          } // else no check necessary, all index values are valid
          ArrayReference<? extends L> existingRef = locks.get(index);
          L existing = existingRef == null ? null : existingRef.get();
          if (existing != null) {
            return existing;
          }
          L created = supplier.get();
          ArrayReference<L> newRef = new ArrayReference<>(created, index, queue);
          L raceWinner = tryInsertOrGetExisting(index, existingRef, newRef);
          if (raceWinner != null) {
            return raceWinner;
          }
          drainQueue();
          return created;
        }

        private L tryInsertOrGetExisting(int index, ArrayReference<? extends L> existingRef, ArrayReference<L> newRef) {
          while (!locks.compareAndSet(index, existingRef, newRef)) {
            existingRef = locks.get(index);
            L existing = existingRef == null ? null : existingRef.get();
            if (existing != null) {
              return existing;
            }
          }
          return null;
        }
}
