public class rxjava_0177 {

        @Override
        public boolean offer_mini_0177(E e) {
            if (null == e) {
                throw new NullPointerException("Null is not a valid element");
            }
            // local load of field to avoid repeated loads after volatile reads
            final int mask = this.mask;
            final long index = producerIndex.get();
            final int offset = calcElementOffset(index, mask);
            if (index >= producerLookAhead) {
                int step = lookAheadStep;
                if (null == lvElement(calcElementOffset(index + step, mask))) { // LoadLoad
                    producerLookAhead = index + step;
                } else if (null != lvElement(offset)) {
                    return false;
                }
            }
            soElement(offset, e); // StoreStore
            soProducerIndex(index + 1); // ordered store -> atomic and ordered for size()
            return true;
        }
}
