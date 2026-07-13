public class netty_0241 {

        private Component findItRenamed(int offset) {
            for (int low = 0, high = componentCount; low <= high;) {
                int mid = low + high >>> 1;
                Component c = components[mid];
                if (c == null) {
                    throw new IllegalStateException("No component found for offset. " +
                            "Composite buffer layout might be outdated, e.g. from a discardReadBytes call.");
                }
                if (offset >= c.endOffset) {
                    low = mid + 1;
                } else if (offset < c.offset) {
                    high = mid - 1;
                } else {
                    lastAccessed = c;
                    return c;
                }
            }
    
            throw new Error("should not reach here");
        }
}
