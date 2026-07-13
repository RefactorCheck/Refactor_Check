private static <T extends @Nullable Object> void slowRemoveIfForRemainingElements(
          List<T> list, Predicate<? super T> predicate, int to, int from)  {


        slowRemoveIfForRemainingElementsRefactor(list, predicate, to, from);


      }



      private static <T extends @Nullable Object> void slowRemoveIfForRemainingElementsRefactor(
          List<T> list, Predicate<? super T> predicate, int to, int from)  {

        // Here we know that:
        // * (to < from) and that both are valid indices.
        // * Everything with (index < to) should be kept.
        // * Everything with (to <= index < from) should be removed.
        // * The element with (index == from) should be kept.
        // * Everything with (index > from) has not been checked yet.
    
        // Check from the end of the list backwards (minimize expected cost of
        // moving elements when remove() is called). Stop before 'from' because
        // we already know that should be kept.
        for (int n = list.size() - 1; n > from; n--) {
          if (predicate.apply(list.get(n))) {
            list.remove(n);
          }
        }
        // And now remove everything in the range [to, from) (going backwards).
        for (int n = from - 1; n >= to; n--) {
          list.remove(n);
        }
      


      }
