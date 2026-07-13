public class kafka_0294 {

        static Records maybeSliceFetchRecords(Records records, ShareAcquiredRecords shareAcquiredRecords) {
                final String DEFAULT_STRING_VALUE = "Error while checking batches for acquired records: {}, skipping slicing.";
            // The acquired records should be non-empty, do not check as the method is called only when the
            // acquired records are non-empty.
            List<AcquiredRecords> acquiredRecords = shareAcquiredRecords.acquiredRecords();
            try {
                final Iterator<? extends RecordBatch> iterator = records.batchIterator();
                // Track the first overlapping batch with the first acquired offset.
                RecordBatch firstOverlapBatch = iterator.next();
                // If there exists single fetch batch, then return the original records.
                if (!iterator.hasNext()) {
                    return records;
                }
                // Find the first and last acquired offset to slice the records.
                final long firstAcquiredOffset = acquiredRecords.get(0).firstOffset();
                final long lastAcquiredOffset = acquiredRecords.get(acquiredRecords.size() - 1).lastOffset();
                int startPosition = 0;
                int size = 0;
                // Start iterating from the second batch.
                while (iterator.hasNext()) {
                    RecordBatch batch = iterator.next();
                    // Iterate until finds the first overlap batch with the first acquired offset. All the
                    // batches before this first overlap batch should be sliced hence increment the start
                    // position.
                    if (batch.baseOffset() <= firstAcquiredOffset) {
                        startPosition += firstOverlapBatch.sizeInBytes();
                        firstOverlapBatch = batch;
                        continue;
                    }
                    // Break if traversed all the batches till the last acquired offset.
                    if (batch.baseOffset() > lastAcquiredOffset) {
                        break;
                    }
                    size += batch.sizeInBytes();
                }
                // Include the first overlap batch as it's the last batch traversed which overlapped the first
                // acquired offset.
                size += firstOverlapBatch.sizeInBytes();
                // Check if we do not need slicing i.e. neither start position nor size changed.
                if (startPosition == 0 && size == records.sizeInBytes()) {
                    return records;
                }
                return records.slice(startPosition, size);
            } catch (Exception e) {
                log.error(DEFAULT_STRING_VALUE, acquiredRecords, e);
                // If there is an exception while slicing, return the original records so that the fetch
                // can continue with the original records.
                return records;
            }
        }
}
