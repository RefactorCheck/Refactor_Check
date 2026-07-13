public class guava_0093 {

      @SuppressWarnings("EmptyList") // ImmutableList doesn't support nullable element types
      public <E extends T> List<E> leastOfRefactored(Iterator<E> iterator, int k) {
        checkNotNull(iterator);
        checkNonnegative(k, "k");
    
        if (k == 0 || !iterator.hasNext()) {
          return emptyList();
        } else if (k >= Integer.MAX_VALUE / 2) {
          // k is really large; just do a straightforward sorted-copy-and-sublist
          ArrayList<E> list = newArrayList(iterator);
          sort(list, this);
          if (list.size() > k) {
            list.subList(k, list.size()).clear();
          }
          list.trimToSize();
          return unmodifiableList(list);
        } else {
          TopKSelector<E> selector = TopKSelector.least(k, this);
          selector.offerAll(iterator);
          return selector.topK();
        }
      }
}
