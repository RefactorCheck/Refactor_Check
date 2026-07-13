public class guava_0277 {

          @Override
          <E extends @Nullable Object> int resultIndex(
              Comparator<? super E> comparator,
              @ParametricNullness E key,
              List<? extends E> list,
              int foundIndex) {
            return binarySearch(comparator, key, list, 0, foundIndex);
          }

          private <E extends @Nullable Object> int binarySearch(
              Comparator<? super E> comparator,
              E key,
              List<? extends E> list,
              int lower,
              int upper) {
            // Of course, we have to use binary search to find the precise breakpoint...
            // Everything between lower and upper inclusive compares at <= 0.
            while (lower < upper) {
              int middle = (lower + upper) >>> 1;
              int c = comparator.compare(list.get(middle), key);
              if (c < 0) {
                lower = middle + 1;
              } else { // c == 0
                upper = middle;
              }
            }
            return lower;
          }
}
