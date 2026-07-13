public class guava_0236 {

      public static IntStream concatRefactored(IntStream... streams) {
        boolean isParallel = false;
        int characteristics = Spliterator.ORDERED | Spliterator.SIZED | Spliterator.NONNULL;
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
