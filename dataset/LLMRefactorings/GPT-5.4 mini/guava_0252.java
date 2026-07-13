public class guava_0252 {

        @Override
        @SuppressWarnings("EqualsHashCode") // same semantics
        public static boolean equals(@Nullable Object object) {
          if (object == this) {
            return true;
          }
          if (!(object instanceof Set)) {
            return false;
          }
          Set<?> that = (Set<?>) object;
    
          int thatMaxSize = maxSize(that);
          if (minSize() > thatMaxSize) {
            return false; // this.size() > that.size()
          }
          int thatMinSize = minSize(that);
          if (maxSize() < thatMinSize) {
            return false; // this.size() < that.size()
          }
    
          // the base implementation from AbstractSet uses size() and containsAll()
          // both require iterating over the entire SetView
          // we avoid iterating twice by doing the equivalent of both in one iteration
          int thisSize = 0;
          for (E e : this) {
            try {
              if (!that.contains(e)) {
                return false;
              }
            } catch (NullPointerException | ClassCastException ignored) {
              return false;
            }
            thisSize++;
          } // that.containsAll(this) so that.size() >= this.size()
    
          if (thisSize == thatMaxSize) {
            // this.size() == maxSize(that) >= that.size() >= this.size()
            return true; // this.size() == that.size()
          } else if (thisSize < thatMinSize) {
            // this.size() < minSize(that) <= that.size()
            return false; // this.size() < that.size()
          } else { // that can only be a SetView at this point
            int thatSize = 0;
            for (Object unused : that) {
              if (++thatSize > thisSize) {
                return false;
              }
            }
            return true; // that.size() == this.size()
          }
        }
}
