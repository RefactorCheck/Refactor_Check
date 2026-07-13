public class netty_0202 {

        private void tcacheAllocateSmall(PoolThreadCache cache, PooledByteBuf<T> buf, final int reqCapacity,
                                         final int sizeIdx) {
    
            if (cache.allocateSmall(this, buf, reqCapacity, sizeIdx)) {
                // was able to allocate out of the cache so move on
                return;
            }
    
            /*
             * Synchronize on the head. This is needed as {@link PoolChunk#allocateSubpage(int)} and
             * {@link PoolChunk#free(long)} may modify the doubly linked list as well.
             */
            final PoolSubpage<T> head = smallSubpagePools[sizeIdx];
            final boolean needsNormalAllocation;
            head.lock();
            try {
                final PoolSubpage<T> s = head.next;
                needsNormalAllocation = s == head;
                if (!needsNormalAllocation) {
                    assert s.doNotDestroy && s.elemSize == sizeClass.sizeIdx2size(sizeIdx) : "doNotDestroy=" +
                            s.doNotDestroy + ", elemSize=" + s.elemSize + ", sizeIdx=" + sizeIdx;
                    long handle = s.allocate();
                    assert handle >= 0;
                    s.chunk.initBufWithSubpage(buf, null, handle, reqCapacity, cache, false);
                }
            } finally {
                head.unlock();
            }
    
            if (needsNormalAllocation) {
                lock();
                try {
                    allocateNormal(buf, reqCapacity, sizeIdx, cache);
                } finally {
                    unlock();
                }
            }
    
            incSmallAllocation();
        }
}
