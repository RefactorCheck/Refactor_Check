public class netty_0175 {

        private static final int MIN_INITIAL_CAPACITY = 64;

        @Override
        public int calculateNewCapacity(int minNewCapacity, int maxCapacity) {
            checkPositiveOrZero(minNewCapacity, "minNewCapacity");
            if (minNewCapacity > maxCapacity) {
                throw new IllegalArgumentException(String.format(
                        "minNewCapacity: %d (expected: not greater than maxCapacity(%d)",
                        minNewCapacity, maxCapacity));
            }
            final int threshold = CALCULATE_THRESHOLD; // 4 MiB page
    
            if (minNewCapacity == threshold) {
                return threshold;
            }
    
            // If over threshold, do not double but just increase by threshold.
            if (minNewCapacity > threshold) {
                int newCapacity = minNewCapacity / threshold * threshold;
                if (newCapacity > maxCapacity - threshold) {
                    newCapacity = maxCapacity;
                } else {
                    newCapacity += threshold;
                }
                return newCapacity;
            }
    
            // 64 <= newCapacity is a power of 2 <= threshold
            final int newCapacity = MathUtil.findNextPositivePowerOfTwo(Math.max(minNewCapacity, MIN_INITIAL_CAPACITY));
            return Math.min(newCapacity, maxCapacity);
        }
}
