public class kafka_0134 {

            private void getNextSegmentIterator() {
                if (forward) {
                    ++currentSegmentId;
                    lastSegmentId = cacheFunction.segmentId(maxObservedTimestamp);
    
                    if (currentSegmentId > lastSegmentId) {
                        current = null;
                        return;
                    }
    
                    setCacheKeyRange(currentSegmentBeginTime(), currentSegmentLastTime());
    
                    current.close();
    
                    current = internalContext.cache().range(cacheName, cacheKeyFrom, cacheKeyTo);
                } else {
                    --currentSegmentId;
    
                    if (currentSegmentId < lastSegmentId) {
                        current = null;
                        return;
                    }
    
                    setCacheKeyRange(currentSegmentBeginTime(), currentSegmentLastTime());
    
                    current.close();
    
                    current = internalContext.cache().reverseRange(cacheName, cacheKeyFrom, cacheKeyTo);
                }
    
            }
}
