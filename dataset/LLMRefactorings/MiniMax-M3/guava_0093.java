public class guava_0093 {

      @SuppressWarnings("EmptyList") // ImmutableList doesn't support nullable element types
      public <E extends T> List<E> leastOf(Iterator<E> iterator, int k) {
        checkNotNull(iterator);
        checkNonnegative(k, "k");
    
        if (k == 0 || !iterator.hasNext()) {
          return emptyList();
        } else if (k >= Integer.MAX_VALUE / 2) {
          return sortedCopyAndSublist(iterator, k);
        } else {
          TopKSelector<E> selector = TopKSelector.least(k, this);
          selector.offerAll(iterator);
          return selector.topK();
        }
      }

      private <E extends T> List<E> sortedCopyAndSublist(Iterator<E> iterator, int k) {
        ArrayList<E> list = newArrayList(iterator);
        sort(list, this);
        if (list.size() > k) {
          list.subList(k, list.size()).clear();
        }
        list.trimToSize();
        return unmodifiableList(list);
      }
}
