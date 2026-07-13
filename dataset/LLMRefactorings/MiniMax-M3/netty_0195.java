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
            } else if (freeIdx == -1 || random.nextBoolean()) {
                freeIdx = bucketIdx;
            }
        }
        if (freeIdx == -1) {
            return -1;
        }
        return createBucketAndGetId(freeIdx);
    }

    private int createBucketAndGetId(int bucketIdx) {
        DnsQueryIdRange bucket = newBucket(bucketIdx, random);
        idBuckets[bucketIdx] = bucket;
        int id = bucket.nextId();
        assert id >= 0;
        return id;
    }
}
