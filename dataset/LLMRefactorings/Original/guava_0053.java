public class guava_0053 {

      public static <R extends @Nullable Object> Stream<R> mapWithIndex(
          IntStream stream, IntFunctionWithIndex<R> function) {
        checkNotNull(stream);
        checkNotNull(function);
        boolean isParallel = stream.isParallel();
        Spliterator.OfInt fromSpliterator = stream.spliterator();
    
        if (!fromSpliterator.hasCharacteristics(Spliterator.SUBSIZED)) {
          PrimitiveIterator.OfInt fromIterator = Spliterators.iterator(fromSpliterator);
          return StreamSupport.stream(
                  new AbstractSpliterator<R>(
                      fromSpliterator.estimateSize(),
                      fromSpliterator.characteristics() & (Spliterator.ORDERED | Spliterator.SIZED)) {
                    long index = 0;
    
                    @Override
                    public boolean tryAdvance(Consumer<? super R> action) {
                      if (fromIterator.hasNext()) {
                        action.accept(function.apply(fromIterator.nextInt(), index++));
                        return true;
                      }
                      return false;
                    }
                  },
                  isParallel)
              .onClose(stream::close);
        }
        final class Splitr extends MapWithIndexSpliterator<Spliterator.OfInt, R, Splitr>
            implements IntConsumer {
          int holder;
    
          Splitr(Spliterator.OfInt splitr, long index) {
            super(splitr, index);
          }
    
          @Override
          public void accept(int t) {
            this.holder = t;
          }
    
          @Override
          public boolean tryAdvance(Consumer<? super R> action) {
            if (fromSpliterator.tryAdvance(this)) {
              action.accept(function.apply(holder, index++));
              return true;
            }
            return false;
          }
    
          @Override
          Splitr createSplit(Spliterator.OfInt from, long i) {
            return new Splitr(from, i);
          }
        }
        return StreamSupport.stream(new Splitr(fromSpliterator, 0), isParallel).onClose(stream::close);
      }
}
