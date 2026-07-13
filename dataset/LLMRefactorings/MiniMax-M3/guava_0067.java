public class guava_0067 {

        @Override
        public int compare(@Nullable String lhs, @Nullable String rhs) {
          if (lhs == rhs) {
            return 0;
          }
          if (lhs == null) {
            return compareNullLeft(rhs);
          }
          if (rhs == null) {
            return compareNullRight(lhs);
          }
          return lhs.compareTo(rhs);
        }

        private int compareNullLeft(String rhs) {
          if (rhs.equals(justAfterNull)) {
            return -1;
          }
          return justAfterNull.compareTo(rhs);
        }

        private int compareNullRight(String lhs) {
          if (lhs.equals(justAfterNull)) {
            return 1;
          }
          return lhs.compareTo(justAfterNull);
        }
}
