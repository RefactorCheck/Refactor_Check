public class guava_0236 {

      private static final int BASE_CHARACTERISTICS =
          Spliterator.ORDERED | Spliterator.SIZED | Spliterator.NONNULL;

      public static IntStream concat(IntStream... streams) {
        boolean isParallel = false;
        int characteristics = BASE_CHARACTERISTICS;
        long estimatedSize = 0L;
        ImmutableList.Builder<Spliterator.OfInt> splitrsBuilder =
            new ImmutableList.Builder<>(streams.length);
        for (IntStream stream : streams) {
          isParallel |= stream.isParallel();
          Spliterator.OfInt splitr = stream.spliterator();
          splitrsBuilder.add(splitr);
          characteristics &= splitr.characteristics();
          estimatedSize = LongMath.saturatedAdd(estimatedSize, splitr.estimateSize());
        }
        return StreamSupport.intStream(
                CollectSpliterators.flatMapToInt(
                    splitrsBuilder.build().spliterator(),
                    splitr -> splitr,
                    characteristics,
                    estimatedSize),
                isParallel)
            .onClose(() -> closeAll(streams));
      }
}
