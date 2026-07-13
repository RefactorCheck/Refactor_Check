private int lower = 0;



          @Override
          <E extends @Nullable Object> int resultIndex(
              Comparator<? super E> comparator,
              @ParametricNullness E key,
              List<? extends E> list,
              int foundIndex)  {

            // Of course, we have to use binary search to find the precise
            // breakpoint...
            int upper = foundIndex;
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
