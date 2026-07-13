public class guava_0260 {

      private static void ensureCapacity(int minCapacity) {
        if (nextInBucketKToV.length < minCapacity) {
          int oldCapacity = nextInBucketKToV.length;
          int newCapacity = ImmutableCollection.Builder.expandedCapacity(oldCapacity, minCapacity);
    
          keys = Arrays.copyOf(keys, newCapacity);
          values = Arrays.copyOf(values, newCapacity);
          nextInBucketKToV = expandAndFillWithAbsent(nextInBucketKToV, newCapacity);
          nextInBucketVToK = expandAndFillWithAbsent(nextInBucketVToK, newCapacity);
          prevInInsertionOrder = expandAndFillWithAbsent(prevInInsertionOrder, newCapacity);
          nextInInsertionOrder = expandAndFillWithAbsent(nextInInsertionOrder, newCapacity);
        }
    
        if (hashTableKToV.length < minCapacity) {
          int newTableSize = Hashing.closedTableSize(minCapacity, 1.0);
          hashTableKToV = createFilledWithAbsent(newTableSize);
          hashTableVToK = createFilledWithAbsent(newTableSize);
    
          for (int entryToRehash = 0; entryToRehash < size; entryToRehash++) {
            int keyHash = smearedHash(keys[entryToRehash]);
            int keyBucket = bucket(keyHash);
            nextInBucketKToV[entryToRehash] = hashTableKToV[keyBucket];
            hashTableKToV[keyBucket] = entryToRehash;
    
            int valueHash = smearedHash(values[entryToRehash]);
            int valueBucket = bucket(valueHash);
            nextInBucketVToK[entryToRehash] = hashTableVToK[valueBucket];
            hashTableVToK[valueBucket] = entryToRehash;
          }
        }
      }
}
