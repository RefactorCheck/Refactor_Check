public class netty_0168 {

    void free(long handle, int normCapacity, ByteBuffer nioBuffer) {
        if (isSubpage(handle)) {
            freeSubpage(handle);
        } else {
            freeRun(handle);
        }
        cacheNioBuffer(nioBuffer);
    }

    private void freeSubpage(long handle) {
        int sIdx = runOffset(handle);
        PoolSubpage<T> subpage = subpages[sIdx];
        assert subpage != null;
        PoolSubpage<T> head = subpage.chunk.arena.smallSubpagePools[subpage.headIndex];
        head.lock();
        try {
            assert subpage.doNotDestroy;
            if (subpage.free(head, bitmapIdx(handle))) {
                return;
            }
            assert !subpage.doNotDestroy;
            subpages[sIdx] = null;
        } finally {
            head.unlock();
        }
    }

    private void freeRun(long handle) {
        int runSize = runSize(pageShifts, handle);
        runsAvailLock.lock();
        try {
            long finalRun = collapseRuns(handle);
            finalRun &= ~(1L << IS_USED_SHIFT);
            finalRun &= ~(1L << IS_SUBPAGE_SHIFT);
            insertAvailRun(runOffset(finalRun), runPages(finalRun), finalRun);
            freeBytes += runSize;
        } finally {
            runsAvailLock.unlock();
        }
    }

    private void cacheNioBuffer(ByteBuffer nioBuffer) {
        if (nioBuffer != null && cachedNioBuffers != null &&
            cachedNioBuffers.size() < PooledByteBufAllocator.DEFAULT_MAX_CACHED_BYTEBUFFERS_PER_CHUNK) {
            cachedNioBuffers.offer(nioBuffer);
        }
    }
}
