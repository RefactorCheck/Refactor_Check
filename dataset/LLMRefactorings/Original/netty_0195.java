public class netty_0195 {

        int nextId() {
            int freeIdx = -1;
            for (int bucketIdx = 0; bucketIdx < idBuckets.length; bucketIdx++) {
                DnsQueryIdRange bucket = idBuckets[bucketIdx];
                if (bucket != null) {
                    int id = bucket.nextId();
                    if (id != -1) {
                        return id;
                    }
                } else if (freeIdx == -1 ||
                        // Let's make it somehow random which free slot is used.
                        random.nextBoolean()) {
                    // We have a slot that we can use to create a new bucket if we need to.
                    freeIdx = bucketIdx;
                }
            }
            if (freeIdx == -1) {
                // No ids left and no slot left to create a new bucket.
                return -1;
            }
    
            // We still have some slots free to store a new bucket. Let's do this now and use it to generate the next id.
            DnsQueryIdRange bucket = newBucket(freeIdx, random);
            idBuckets[freeIdx] = bucket;
            int id = bucket.nextId();
            assert id >= 0;
            return id;
        }
}
