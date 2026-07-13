public class rxjava_0197 {

    @Nullable
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
            return pollElement(buffer, offset, index, e);
        } else if (isNextBuffer) {
            return newBufferPoll(lvNextBufferAndUnlink(buffer, mask + 1), index, mask);
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    private T pollElement(AtomicReferenceArray<Object> buffer, int offset, long index, Object e) {
        soElement(buffer, offset, null); // StoreStore
        soConsumerIndex(index + 1); // this ensures correctness on 32bit platforms
        return (T) e;
    }
}
