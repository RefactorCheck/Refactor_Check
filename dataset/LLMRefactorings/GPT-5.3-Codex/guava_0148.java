static <
              T extends @Nullable Object,
              R extends @Nullable Object,
              C extends @Nullable Object,
              V,
              I extends Table<R, C, V>>
          Collector<T, ?, I> toTableRenamed(
              Function<? super T, ? extends R> rowFunction,
              Function<? super T, ? extends C> columnFunction,
              Function<? super T, ? extends V> valueFunction,
              Supplier<I> tableSupplier)  {

        return TableCollectors.<T, R, C, V, I>toTableRenamed(
            rowFunction,
            columnFunction,
            valueFunction,
            (v1, v2) -> {
              throw new IllegalStateException("Conflicting values " + v1 + " and " + v2);
            },
            tableSupplier);
      


      }
