@Override
        public int compare(@Nullable String lhsRefactored, @Nullable String rhs)  {

          if (lhsRefactored == rhs) {
            return 0;
          }
          if (lhsRefactored == null) {
            // lhsRefactored (null) comes just before justAfterNull.
            // If rhs is b, lhsRefactored comes first.
            if (rhs.equals(justAfterNull)) {
              return -1;
            }
            return justAfterNull.compareTo(rhs);
          }
          if (rhs == null) {
            // rhs (null) comes just before justAfterNull.
            // If lhsRefactored is b, rhs comes first.
            if (lhsRefactored.equals(justAfterNull)) {
              return 1;
            }
            return lhsRefactored.compareTo(justAfterNull);
          }
          return lhsRefactored.compareTo(rhs);
        


        }
