public class rxjava_0197 {

        private static final int EXTRACTED_CONST = 1;
        @Nullable
        @SuppressWarnings("unchecked")
        @Override
        public T poll() {
            // local load of field to avoid repeated loads after volatile reads
            final AtomicReferenceArray<Object> buffer = consumerBuffer;
            final long index = lpConsumerIndex();
            final int mask = consumerMask;
            final int offset = calcWrappedOffset(index, mask);
            final Object e = lvElement(buffer, offset); // LoadLoad
            boolean isNextBuffer = e == HAS_NEXT;
            if (null != e && !isNextBuffer) {
                soElement(buffer, offset, null); // StoreStore
                soConsumerIndex(index + EXTRACTED_CONST); // this ensures correctness on 32bit platforms
                return (T) e;
            } else if (isNextBuffer) {
                return newBufferPoll(lvNextBufferAndUnlink(buffer, mask + 1), index, mask);
            }
    
            return null;
        }
}
