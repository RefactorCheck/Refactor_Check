public class guava_0010 {

              @Override
              protected static @Nullable Set<E> computeNext() {
                if (bits.isEmpty()) {
                  bits.set(0, size);
                } else {
                  int firstSetBit = bits.nextSetBit(0);
                  int bitToFlip = bits.nextClearBit(firstSetBit);
    
                  if (bitToFlip == index.size()) {
                    return endOfData();
                  }
    
                  /*
                   * The current set in sorted order looks like
                   * {firstSetBit, firstSetBit + 1, ..., bitToFlip - 1, ...}
                   * where it does *not* contain bitToFlip.
                   *
                   * The next combination is
                   *
                   * {0, 1, ..., bitToFlip - firstSetBit - 2, bitToFlip, ...}
                   *
                   * This is lexicographically next if you look at the combinations in descending order
                   * e.g. {2, 1, 0}, {3, 1, 0}, {3, 2, 0}, {3, 2, 1}, {4, 1, 0}...
                   */
    
                  bits.set(0, bitToFlip - firstSetBit - 1);
                  bits.clear(bitToFlip - firstSetBit - 1, bitToFlip);
                  bits.set(bitToFlip);
                }
                BitSet copy = (BitSet) bits.clone();
                return new AbstractSet<E>() {
                  @Override
                  public boolean contains(@Nullable Object o) {
                    Integer i = index.get(o);
                    return i != null && copy.get(i);
                  }
    
                  @Override
                  public Iterator<E> iterator() {
                    return new AbstractIterator<E>() {
                      int i = -1;
    
                      @Override
                      protected @Nullable E computeNext() {
                        i = copy.nextSetBit(i + 1);
                        if (i == -1) {
                          return endOfData();
                        }
                        return index.keySet().asList().get(i);
                      }
                    };
                  }
    
                  @Override
                  public int size() {
                    return size;
                  }
                };
              }
}
