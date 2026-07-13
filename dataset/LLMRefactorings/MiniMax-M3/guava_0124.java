public class guava_0124 {

      @CanIgnoreReturnValue
      public Ordered expect(Iterable<?> elements) {
        List<List<E>> resultsForAllStrategies = new ArrayList<>();
        for (Supplier<GeneralSpliterator<E>> spliteratorSupplier : spliteratorSuppliers) {
          GeneralSpliterator<E> spliterator = spliteratorSupplier.get();
          int characteristics = spliterator.characteristics();
          long estimatedSize = spliterator.estimateSize();
          for (SpliteratorDecompositionStrategy strategy :
              SpliteratorDecompositionStrategy.ALL_STRATEGIES) {
            List<E> resultsForStrategy = new ArrayList<>();
            strategy.forEach(spliteratorSupplier.get(), resultsForStrategy::add);

            validateStrategyResults(spliterator, characteristics, estimatedSize, resultsForStrategy);

            assertEqualIgnoringOrder(elements, resultsForStrategy);
            resultsForAllStrategies.add(resultsForStrategy);
          }
        }
        return new Ordered() {
          @Override
          public void inOrder() {
            for (List<E> resultsForStrategy : resultsForAllStrategies) {
              assertEqualInOrder(elements, resultsForStrategy);
            }
          }
        };
      }

      private void validateStrategyResults(
          GeneralSpliterator<E> spliterator,
          int characteristics,
          long estimatedSize,
          List<E> resultsForStrategy) {
        if ((characteristics & Spliterator.NONNULL) != 0) {
          assertFalse(resultsForStrategy.contains(null));
        }
        if ((characteristics & Spliterator.SORTED) != 0) {
          Comparator<? super E> comparator = spliterator.getComparator();
          if (comparator == null) {
            @SuppressWarnings({"unchecked", "rawtypes"})
            Comparator<? super E> naturalOrder =
                (Comparator<? super E>) Comparator.<Comparable>naturalOrder();
            comparator = naturalOrder;
          }
          assertTrue(Ordering.from(comparator).isOrdered(resultsForStrategy));
        }
        if ((characteristics & Spliterator.SIZED) != 0) {
          assertEquals(Ints.checkedCast(estimatedSize), resultsForStrategy.size());
        }
      }
}
