public class guava_0251 {

      public Iterable<B> convertAll(Iterable<? extends A> fromIterable) {
        checkNotNull(fromIterable, "fromIterable");
        return () -> createIterator(fromIterable);
      }

      private Iterator<B> createIterator(Iterable<? extends A> fromIterable) {
        return new Iterator<B>() {
          private final Iterator<? extends A> fromIterator = fromIterable.iterator();

          @Override
          public boolean hasNext() {
            return fromIterator.hasNext();
          }

          @Override
          public B next() {
            return convert(fromIterator.next());
          }

          @Override
          public void remove() {
            fromIterator.remove();
          }
        };
      }
}
