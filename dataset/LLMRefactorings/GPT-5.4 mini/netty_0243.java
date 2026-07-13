public class netty_0243 {

            @Override
            public int pollUpdated() {
                final long cIndex = consumerIndex;
                final int offset = (int) (cIndex & mask);
                // If we can't see the next available element we can't poll
                int value = get(offset);
                if (emptyValue == value) {
                    /*
                     * NOTE: Queue may not actually be empty in the case of a producer (P1) being interrupted after
                     * winning the CAS on offer but before storing the element in the queue. Other producers may go on
                     * to fill up the queue after this element.
                     */
                    if (cIndex != producerIndex) {
                        do {
                            value = get(offset);
                        } while (emptyValue == value);
                    } else {
                        return emptyValue;
                    }
                }
                lazySet(offset, emptyValue);
                CONSUMER_INDEX.lazySet(this, cIndex + 1);
                return value;
            }
}
