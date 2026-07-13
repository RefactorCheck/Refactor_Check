public class rxjava_0054 {

        @Override
        public boolean offer(final T e) {
            if (null == e) {
                throw new NullPointerException("Null is not a valid element");
            }
            // local load of field to avoid repeated loads after volatile reads
            final AtomicReferenceArray<Object> buffer = producerBuffer;
            final long index = lpProducerIndex();
            final int mask = producerMask;
            final int offset = calcWrappedOffset(index, mask);
            if (index < producerLookAhead) {
                return writeToQueue(buffer, e, index, offset);
            } else {
                final int lookAheadStep = producerLookAheadStep;
                // go around the buffer or resize if full (unless we hit max capacity)
                int lookAheadElementOffset = calcWrappedOffset(index + lookAheadStep, mask);
                if (null == lvElement(buffer, lookAheadElementOffset)) { // LoadLoad
                    producerLookAhead = index + lookAheadStep - 1; // joy, there's plenty of room
                    return writeToQueue(buffer, e, index, offset);
                } else if (null == lvElement(buffer, calcWrappedOffset(index + 1, mask))) { // buffer is not full
                    return writeToQueue(buffer, e, index, offset);
                } else {
                    resize(buffer, index, offset, e, mask); // add a buffer and link old to new
                    return true;
                }
            }
        }
}
