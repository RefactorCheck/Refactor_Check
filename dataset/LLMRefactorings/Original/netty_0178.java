public class netty_0178 {

            @Override
            public int fill(int limit, IntSupplier supplier) {
                Objects.requireNonNull(supplier, "supplier");
                ObjectUtil.checkPositiveOrZero(limit, "limit");
                if (limit == 0) {
                    return 0;
                }
                final int mask = this.mask;
                final long capacity = mask + 1;
                long producerLimit = this.producerLimit;
                long pIndex;
                int actualLimit;
                do {
                    pIndex = producerIndex;
                    long available = producerLimit - pIndex;
                    if (available <= 0) {
                        final long cIndex = consumerIndex;
                        producerLimit = cIndex + capacity;
                        available = producerLimit - pIndex;
                        if (available <= 0) {
                            // FULL :(
                            return 0;
                        } else {
                            // update producer limit to the next index that we must recheck the consumer index
                            PRODUCER_LIMIT.lazySet(this, producerLimit);
                        }
                    }
                    actualLimit = Math.min((int) available, limit);
                } while (!PRODUCER_INDEX.compareAndSet(this, pIndex, pIndex + actualLimit));
                // right, now we claimed a few slots and can fill them with goodness
                for (int i = 0; i < actualLimit; i++) {
                    // Won CAS, move on to storing
                    final int offset = (int) (pIndex + i & mask);
                    lazySet(offset, supplier.getAsInt());
                }
                return actualLimit;
            }
}
