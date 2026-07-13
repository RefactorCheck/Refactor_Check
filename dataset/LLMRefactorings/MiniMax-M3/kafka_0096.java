public class kafka_0096 {

    private boolean maybeUpdateHighWatermark() {
        ArrayList<ReplicaState> followersByDescendingFetchOffset = followersByDescendingFetchOffset()
            .collect(Collectors.toCollection(ArrayList::new));

        int indexOfHw = voterStates.size() / 2;
        Optional<LogOffsetMetadata> highWatermarkUpdateOpt = followersByDescendingFetchOffset.get(indexOfHw).endOffset;

        if (highWatermarkUpdateOpt.isPresent()) {
            LogOffsetMetadata highWatermarkUpdateMetadata = highWatermarkUpdateOpt.get();
            long highWatermarkUpdateOffset = highWatermarkUpdateMetadata.offset();

            if (highWatermarkUpdateOffset > epochStartOffset) {
                if (highWatermark.isPresent()) {
                    LogOffsetMetadata currentHighWatermarkMetadata = highWatermark.get();
                    if (highWatermarkUpdateOffset > currentHighWatermarkMetadata.offset()
                        || (highWatermarkUpdateOffset == currentHighWatermarkMetadata.offset() &&
                            !highWatermarkUpdateMetadata.metadata().equals(currentHighWatermarkMetadata.metadata()))) {
                        applyHighWatermarkUpdate(highWatermarkUpdateOpt, highWatermarkUpdateMetadata, indexOfHw, followersByDescendingFetchOffset);
                        return true;
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
                    applyHighWatermarkUpdate(highWatermarkUpdateOpt, highWatermarkUpdateMetadata, indexOfHw, followersByDescendingFetchOffset);
                    return true;
                }
            }
        }
        return false;
    }

    private void applyHighWatermarkUpdate(Optional<LogOffsetMetadata> highWatermarkUpdateOpt,
                                          LogOffsetMetadata highWatermarkUpdateMetadata,
                                          int indexOfHw,
                                          ArrayList<ReplicaState> followersByDescendingFetchOffset) {
        Optional<LogOffsetMetadata> oldHighWatermark = highWatermark;
        highWatermark = highWatermarkUpdateOpt;
        logHighWatermarkUpdate(
            oldHighWatermark,
            highWatermarkUpdateMetadata,
            indexOfHw,
            followersByDescendingFetchOffset
        );
    }
}
