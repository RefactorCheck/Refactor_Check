public class guava_0122 {

      @Override
      public int lastIndexOf(@Nullable Object o) {
        if (!(o instanceof List)) {
          return -1;
        }
        List<?> list = (List<?>) o;
        if (list.size() != axes.size()) {
          return -1;
        }
        ListIterator<?> itr = list.listIterator();
        int computedIndex = 0;
        while (itr.hasNext()) {
          int axisIndex = itr.nextIndex();
          int elemIndex = axes.get(axisIndex).lastIndexOf(itr.next());
          if (elemIndex == -1) {
            return -1;
          }
          computedIndex += elemIndex * axesSizeProduct[axisIndex + 1];
        }
        return computedIndex;
      }
}
