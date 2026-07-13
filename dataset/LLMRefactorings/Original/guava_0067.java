public class guava_0067 {

        @Override
        public int compare(@Nullable String lhs, @Nullable String rhs) {
          if (lhs == rhs) {
            return 0;
          }
          if (lhs == null) {
            // lhs (null) comes just before justAfterNull.
            // If rhs is b, lhs comes first.
            if (rhs.equals(justAfterNull)) {
              return -1;
            }
            return justAfterNull.compareTo(rhs);
          }
          if (rhs == null) {
            // rhs (null) comes just before justAfterNull.
            // If lhs is b, rhs comes first.
            if (lhs.equals(justAfterNull)) {
              return 1;
            }
            return lhs.compareTo(justAfterNull);
          }
          return lhs.compareTo(rhs);
        }
}
