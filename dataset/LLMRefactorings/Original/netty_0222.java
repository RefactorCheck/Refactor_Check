public class netty_0222 {

            @Override
            public int drain(int limit, IntConsumer consumer) {
                Objects.requireNonNull(consumer, "consumer");
                ObjectUtil.checkPositiveOrZero(limit, "limit");
                if (limit == 0) {
                    return 0;
                }
                final int mask = this.mask;
                final long cIndex = consumerIndex; // Note: could be weakened to plain-load.
                for (int i = 0; i < limit; i++) {
                    final long index = cIndex + i;
                    final int offset = (int) (index & mask);
                    final int value = get(offset);
                    if (emptyValue == value) {
                        return i;
                    }
                    lazySet(offset, emptyValue); // Note: could be weakened to plain-store.
                    // ordered store -> atomic and ordered for size()
                    CONSUMER_INDEX.lazySet(this, index + 1);
                    consumer.accept(value);
                }
                return limit;
            }
}
