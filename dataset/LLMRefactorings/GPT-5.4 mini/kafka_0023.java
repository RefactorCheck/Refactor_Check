public class kafka_0023 {

        private Optional<Long> findRedundantOffset() {
            if (offsets.isEmpty()) {
                return Optional.empty();
            }
    
            long minimumOffset = Long.MAX_VALUE;
    
            for (long offset : offsets.values()) {
                // Get min offset among latest offsets
                // for all share keys in the internal partition.
                minimumOffset = Math.min(minimumOffset, offset);
    
                // lastRedundantOffset represents the smallest necessary offset
                // and if soFar equals it, we cannot proceed. This can happen
                // if a share partition key hasn't had records written for a while.
                // For example,
                // <p>
                // key1:1
                // key2:2 4 6
                // key3:3 5 7
                // <p>
                // We can see in above that offsets 2, 4, 3, 5 are redundant,
                // but we do not have a contiguous prefix starting at lastRedundantOffset
                // and we cannot proceed.
                if (minimumOffset == lastRedundantOffset.get()) {
                    return Optional.of(minimumOffset);
                }
            }
    
            return Optional.of(minimumOffset);
        }
}
