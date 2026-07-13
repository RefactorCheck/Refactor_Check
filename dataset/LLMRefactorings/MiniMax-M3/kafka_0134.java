public class kafka_0134 {

            private void getNextSegmentIterator() {
                if (forward) {
                    ++currentSegmentId;
                    lastSegmentId = cacheFunction.segmentId(maxObservedTimestamp);
    
                    if (currentSegmentId > lastSegmentId) {
                        current = null;
                        return;
                    }
    
                    setCurrentSegment(false);
                } else {
                    --currentSegmentId;
    
                    if (currentSegmentId < lastSegmentId) {
                        current = null;
                        return;
                    }
    
                    setCurrentSegment(true);
                }
            }

            private void setCurrentSegment(boolean reverse) {
                setCacheKeyRange(currentSegmentBeginTime(), currentSegmentLastTime());
                current.close();
                if (reverse) {
                    current = internalContext.cache().reverseRange(cacheName, cacheKeyFrom, cacheKeyTo);
                } else {
                    current = internalContext.cache().range(cacheName, cacheKeyFrom, cacheKeyTo);
                }
            }
}
