public class guava_0021 {

    @SuppressWarnings("unchecked") // could probably be avoided with a forwarding Spliterator
    @SafeVarargs
    public static <T extends @Nullable Object> Stream<T> concat(Stream<? extends T>... streams) {
        StreamConcatAnalyzer<T> analyzer = analyze(streams);
        return StreamSupport.stream(
                CollectSpliterators.flatMap(
                    analyzer.splitrs.spliterator(),
                    splitr -> (Spliterator<T>) splitr,
                    analyzer.characteristics,
                    analyzer.estimatedSize),
                analyzer.isParallel)
            .onClose(() -> closeAll(streams));
    }

    // TODO(lowasser): consider an implementation that can support SUBSIZED
    private static <T> StreamConcatAnalyzer<T> analyze(Stream<? extends T>[] streams) {
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
        return new StreamConcatAnalyzer<>(splitrsBuilder.build(), characteristics, estimatedSize, isParallel);
    }

    private static class StreamConcatAnalyzer<T> {
        final ImmutableList<Spliterator<? extends T>> splitrs;
        final int characteristics;
        final long estimatedSize;
        final boolean isParallel;

        StreamConcatAnalyzer(ImmutableList<Spliterator<? extends T>> splitrs,
                             int characteristics,
                             long estimatedSize,
                             boolean isParallel) {
            this.splitrs = splitrs;
            this.characteristics = characteristics;
            this.estimatedSize = estimatedSize;
            this.isParallel = isParallel;
        }
    }
}
