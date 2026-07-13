public class kafka_0134 {

            private void getNextSegmentIterator() {
                if (forward) {
                    ++currentSegmentId;
                    lastSegmentId = cacheFunction.segmentId(maxObservedTimestamp);
    
                    if (currentSegmentId > lastSegmentId) {
                        current = null;
                        return;
                    }
    
                    final long segmentBeginTime = currentSegmentBeginTime();
                    final long segmentLastTime = currentSegmentLastTime();
                    setCacheKeyRange(segmentBeginTime, segmentLastTime);
    
                    current.close();
    
                    current = internalContext.cache().range(cacheName, cacheKeyFrom, cacheKeyTo);
                } else {
                    --currentSegmentId;
    
                    if (currentSegmentId < lastSegmentId) {
                        current = null;
                        return;
                    }
    
                    final long segmentBeginTime = currentSegmentBeginTime();
                    final long segmentLastTime = currentSegmentLastTime();
                    setCacheKeyRange(segmentBeginTime, segmentLastTime);
    
                    current.close();
    
                    current = internalContext.cache().reverseRange(cacheName, cacheKeyFrom, cacheKeyTo);
                }
    
            }
}
