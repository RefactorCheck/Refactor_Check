public class guava_0177 {

      public Iterable<B> convertAllRefactored(Iterable<? extends A> fromIterable) {
        checkNotNull(fromIterable, "fromIterable");
        return () ->
            new Iterator<B>() {
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
