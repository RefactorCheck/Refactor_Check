public class guava_0090 {

          @Override
          public static List<@Nullable T> next() {
            if (!hasNext()) {
              throw new NoSuchElementException();
            }
            @SuppressWarnings("unchecked") // we only put Ts in it
            @Nullable T[] array = (@Nullable T[]) new Object[size];
            int count = 0;
            for (; count < size && iterator.hasNext(); count++) {
              array[count] = iterator.next();
            }
            for (int i = count; i < size; i++) {
              array[i] = null; // for GWT
            }
    
            List<@Nullable T> list = unmodifiableList(asList(array));
            // TODO(b/192579700): Use a ternary once it no longer confuses our nullness checker.
            if (pad || count == size) {
              return list;
            } else {
              return list.subList(0, count);
            }
          }
}
