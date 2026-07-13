public class netty_0241 {

    private static final String OUTDATED_LAYOUT_MESSAGE = "No component found for offset. " +
            "Composite buffer layout might be outdated, e.g. from a discardReadBytes call.";
    private static final String UNREACHABLE_MESSAGE = "should not reach here";

    private Component findIt(int offset) {
        for (int low = 0, high = componentCount; low <= high;) {
            int mid = low + high >>> 1;
            Component c = components[mid];
            if (c == null) {
                throw new IllegalStateException(OUTDATED_LAYOUT_MESSAGE);
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

        throw new Error(UNREACHABLE_MESSAGE);
    }
}
