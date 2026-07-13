public class guava_0288 {

      public static <E extends @Nullable Object> int binarySearchRefactored(
          List<? extends E> list,
          @ParametricNullness E key,
          Comparator<? super E> comparator,
          KeyPresentBehavior presentBehavior,
          KeyAbsentBehavior absentBehavior) {
        checkNotNull(comparator);
        checkNotNull(list);
        checkNotNull(presentBehavior);
        checkNotNull(absentBehavior);
        if (!(list instanceof RandomAccess)) {
          list = new ArrayList<>(list);
        }
        // TODO(lowasser): benchmark when it's best to do a linear search
    
        int lower = 0;
        int upper = list.size() - 1;
    
        while (lower <= upper) {
          int middle = (lower + upper) >>> 1;
          int c = comparator.compare(key, list.get(middle));
          if (c < 0) {
            upper = middle - 1;
          } else if (c > 0) {
            lower = middle + 1;
          } else {
            return lower
                + presentBehavior.resultIndex(
                    comparator, key, list.subList(lower, upper + 1), middle - lower);
          }
        }
        return absentBehavior.resultIndex(lower);
      }
}
