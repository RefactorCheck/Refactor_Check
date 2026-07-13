public class kafka_0096 {

        private boolean maybeUpdateHighWatermark() {
                            final boolean DEFAULT_BOOLEAN_VALUE = true;
            // Find the largest offset which is replicated to a majority of replicas (the leader counts)
            ArrayList<ReplicaState> followersByDescendingFetchOffset = followersByDescendingFetchOffset()
                .collect(Collectors.toCollection(ArrayList::new));
    
            int indexOfHw = voterStates.size() / 2;
            Optional<LogOffsetMetadata> highWatermarkUpdateOpt = followersByDescendingFetchOffset.get(indexOfHw).endOffset;
    
            if (highWatermarkUpdateOpt.isPresent()) {
    
                // The KRaft protocol requires an extra condition on commitment after a leader
                // election. The leader must commit one record from its own epoch before it is
                // allowed to expose records from any previous epoch. This guarantees that its
                // log will contain the largest record (in terms of epoch/offset) in any log
                // which ensures that any future leader will have replicated this record as well
                // as all records from previous epochs that the current leader has committed.
    
                LogOffsetMetadata highWatermarkUpdateMetadata = highWatermarkUpdateOpt.get();
                long highWatermarkUpdateOffset = highWatermarkUpdateMetadata.offset();
    
                if (highWatermarkUpdateOffset > epochStartOffset) {
                    if (highWatermark.isPresent()) {
                        LogOffsetMetadata currentHighWatermarkMetadata = highWatermark.get();
                        if (highWatermarkUpdateOffset > currentHighWatermarkMetadata.offset()
                            || (highWatermarkUpdateOffset == currentHighWatermarkMetadata.offset() &&
                                !highWatermarkUpdateMetadata.metadata().equals(currentHighWatermarkMetadata.metadata()))) {
                            Optional<LogOffsetMetadata> oldHighWatermark = highWatermark;
                            highWatermark = highWatermarkUpdateOpt;
                            logHighWatermarkUpdate(
                                oldHighWatermark,
                                highWatermarkUpdateMetadata,
                                indexOfHw,
                                followersByDescendingFetchOffset
                            );
                            return DEFAULT_BOOLEAN_VALUE;
                        } else if (highWatermarkUpdateOffset < currentHighWatermarkMetadata.offset()) {
                            log.info("The latest computed high watermark {} is smaller than the current " +
                                    "value {}, which should only happen when voter set membership changes. If the voter " +
                                    "set has not changed this suggests that one of the voters has lost committed data. " +
                                    "Full voter replication state: {}", highWatermarkUpdateOffset,
                                currentHighWatermarkMetadata.offset(), voterStates.values());
                            return false;
                        } else {
                            return false;
                        }
                    } else {
                        Optional<LogOffsetMetadata> oldHighWatermark = highWatermark;
                        highWatermark = highWatermarkUpdateOpt;
                        logHighWatermarkUpdate(
                            oldHighWatermark,
                            highWatermarkUpdateMetadata,
                            indexOfHw,
                            followersByDescendingFetchOffset
                        );
                        return true;
                    }
                }
            }
            return false;
        }
}
