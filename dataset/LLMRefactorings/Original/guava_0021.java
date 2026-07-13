public class guava_0021 {

      @SuppressWarnings("unchecked") // could probably be avoided with a forwarding Spliterator
      @SafeVarargs
      public static <T extends @Nullable Object> Stream<T> concat(Stream<? extends T>... streams) {
        // TODO(lowasser): consider an implementation that can support SUBSIZED
        boolean isParallel = false;
        int characteristics = Spliterator.ORDERED | Spliterator.SIZED | Spliterator.NONNULL;
        long estimatedSize = 0L;
        ImmutableList.Builder<Spliterator<? extends T>> splitrsBuilder =
            new ImmutableList.Builder<>(streams.length);
        for (Stream<? extends T> stream : streams) {
          isParallel |= stream.isParallel();
          Spliterator<? extends T> splitr = stream.spliterator();
          splitrsBuilder.add(splitr);
          characteristics &= splitr.characteristics();
          estimatedSize = LongMath.saturatedAdd(estimatedSize, splitr.estimateSize());
        }
        return StreamSupport.stream(
                CollectSpliterators.flatMap(
                    splitrsBuilder.build().spliterator(),
                    splitr -> (Spliterator<T>) splitr,
                    characteristics,
                    estimatedSize),
                isParallel)
            .onClose(() -> closeAll(streams));
      }
}
