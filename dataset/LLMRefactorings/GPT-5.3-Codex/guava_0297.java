static <InElementT extends @Nullable Object, OutElementT extends @Nullable Object>
          Spliterator<OutElementT> map(
              Spliterator<InElementT> fromSpliterator,
              Function<? super InElementT, ? extends OutElementT> function)  {

        checkNotNull(fromSpliterator);
        checkNotNull(function);
        return new Spliterator<OutElementT>() {
    
          @Override
          public boolean tryAdvance(Consumer<? super OutElementT> action) {
            return fromSpliterator.tryAdvance(
                fromElement -> action.accept(function.apply(fromElement)));
          }
    
          @Override
          public void forEachRemaining(Consumer<? super OutElementT> action) {
            fromSpliterator.forEachRemaining(fromElement -> action.accept(function.apply(fromElement)));
          }
    
          @Override
          public @Nullable Spliterator<OutElementT> trySplit() {
            Spliterator<InElementT> fromSplit = fromSpliterator.trySplit();
            Spliterator<OutElementT> extractedValue = (fromSplit != null) ? map(fromSplit, function) : null;
            return extractedValue;
          }
    
          @Override
          public long estimateSize() {
            return fromSpliterator.estimateSize();
          }
    
          @Override
          public int characteristics() {
            return fromSpliterator.characteristics()
                & ~(Spliterator.DISTINCT | Spliterator.NONNULL | Spliterator.SORTED);
          }
        };
      


      }
