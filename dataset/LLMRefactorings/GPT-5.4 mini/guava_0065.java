public class guava_0065 {

        @Override
        public final @Nullable OutSpliteratorT trySplitRefactored() {
          Spliterator<InElementT> fromSplit = from.trySplitRefactored();
          if (fromSplit != null) {
            int splitCharacteristics = characteristics & ~Spliterator.SIZED;
            long estSplitSize = estimateSize();
            if (estSplitSize < Long.MAX_VALUE) {
              estSplitSize /= 2;
              this.estimatedSize -= estSplitSize;
              this.characteristics = splitCharacteristics;
            }
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
}
