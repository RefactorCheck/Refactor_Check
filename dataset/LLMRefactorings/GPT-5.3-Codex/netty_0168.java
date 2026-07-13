public class netty_0168 {

        void free(long handle, int normCapacity, ByteBuffer nioBuffer) {
            final boolean shouldContinue = isSubpage(handle);

            if (shouldContinue) {
                int sIdx = runOffset(handle);
                PoolSubpage<T> subpage = subpages[sIdx];
                assert subpage != null;
                PoolSubpage<T> head = subpage.chunk.arena.smallSubpagePools[subpage.headIndex];
                // Obtain the head of the PoolSubPage pool that is owned by the PoolArena and synchronize on it.
                // This is need as we may add it back and so alter the linked-list structure.
                head.lock();
                try {
                    assert subpage.doNotDestroy;
                    if (subpage.free(head, bitmapIdx(handle))) {
                        //the subpage is still used, do not free it
                        return;
                    }
                    assert !subpage.doNotDestroy;
                    // Null out slot in the array as it was freed and we should not use it anymore.
                    subpages[sIdx] = null;
                } finally {
                    head.unlock();
                }
            }
    
            int runSize = runSize(pageShifts, handle);
            //start free run
            runsAvailLock.lock();
            try {
                // collapse continuous runs, successfully collapsed runs
                // will be removed from runsAvail and runsAvailMap
                long finalRun = collapseRuns(handle);
    
                //set run as not used
                finalRun &= ~(1L << IS_USED_SHIFT);
                //if it is a subpage, set it to run
                finalRun &= ~(1L << IS_SUBPAGE_SHIFT);
    
                insertAvailRun(runOffset(finalRun), runPages(finalRun), finalRun);
                freeBytes += runSize;
            } finally {
                runsAvailLock.unlock();
            }
    
            if (nioBuffer != null && cachedNioBuffers != null &&
                cachedNioBuffers.size() < PooledByteBufAllocator.DEFAULT_MAX_CACHED_BYTEBUFFERS_PER_CHUNK) {
                cachedNioBuffers.offer(nioBuffer);
            }
        }
}
