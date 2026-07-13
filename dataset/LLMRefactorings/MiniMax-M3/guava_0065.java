public class guava_0065 {

        @Override
        public final @Nullable OutSpliteratorT trySplit() {
          Spliterator<InElementT> fromSplit = from.trySplit();
          if (fromSplit != null) {
            int splitCharacteristics = characteristics & ~Spliterator.SIZED;
            long estSplitSize = estimateSize();
            estSplitSize = adjustEstimates(estSplitSize, splitCharacteristics);
            OutSpliteratorT result =
                factory.newFlatMapSpliterator(
                    this.prefix, fromSplit, function, splitCharacteristics, estSplitSize);
            this.prefix = null;
            return result;
          } else if (prefix != null) {
            OutSpliteratorT result = prefix;
            this.prefix = null;
            return result;
          } else {
            return null;
          }
        }

        private long adjustEstimates(long estSplitSize, int splitCharacteristics) {
          if (estSplitSize < Long.MAX_VALUE) {
            estSplitSize /= 2;
            this.estimatedSize -= estSplitSize;
            this.characteristics = splitCharacteristics;
          }
          return estSplitSize;
        }
}
