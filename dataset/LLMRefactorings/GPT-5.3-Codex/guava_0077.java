private static Iterable<@Nullable Object> iterableRenamed(
          @Nullable Object first, @Nullable Object second, @Nullable Object[] rest)  {

        checkNotNull(rest);
        return new AbstractList<@Nullable Object>() {
          @Override
          public int size() {
            return rest.length + 2;
          }
    
          @Override
          public @Nullable Object get(int index) {
            switch (index) {
              case 0:
                return first;
              case 1:
                return second;
              default:
                return rest[index - 2];
            }
          }
        };
      


      }
