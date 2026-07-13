public class netty_0222 {

    @Override
    public int drain(int limit, IntConsumer consumer) {
        Objects.requireNonNull(consumer, "consumer");
        ObjectUtil.checkPositiveOrZero(limit, "limit");
        if (limit == 0) {
            return 0;
        }
        final int mask = this.mask;
        final long cIndex = consumerIndex;
        final int empty = emptyValue;
        for (int i = 0; i < limit; i++) {
            final long index = cIndex + i;
            final int offset = (int) (index & mask);
            final int value = get(offset);
            if (empty == value) {
                return i;
            }
            lazySet(offset, empty);
            CONSUMER_INDEX.lazySet(this, index + 1);
            consumer.accept(value);
        }
        return limit;
    }
}
