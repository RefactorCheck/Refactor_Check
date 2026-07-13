public class kafka_0212 {

    void publishLogDelta(MetadataImage newImage, LogDeltaManifest manifest) {
        bytesSinceLastSnapshot += manifest.numBytes();
        if (bytesSinceLastSnapshot >= maxBytesSinceLastSnapshot) {
            scheduleSnapshotIfPossible(
                "we have replayed at least " + maxBytesSinceLastSnapshot + " bytes",
                "Not scheduling bytes-based snapshot because event queue is not empty yet.",
                newImage, manifest);
        } else if (maxTimeSinceLastSnapshotNs != 0 &&
                (time.nanoseconds() - lastSnapshotTimeNs >= maxTimeSinceLastSnapshotNs)) {
            scheduleSnapshotIfPossible(
                "we have waited at least " +
                    TimeUnit.NANOSECONDS.toMinutes(maxTimeSinceLastSnapshotNs) +
                    " minute(s)",
                "Not scheduling time-based snapshot because event queue is not empty yet.",
                newImage, manifest);
        } else if (log.isTraceEnabled()) {
            log.trace("Neither time-based nor bytes-based criteria are met; not scheduling snapshot.");
        }
    }

    private void scheduleSnapshotIfPossible(String reason, String skipReason,
                                           MetadataImage newImage, LogDeltaManifest manifest) {
        if (eventQueue.isEmpty()) {
            maybeScheduleEmit(reason, newImage, manifest.provenance().isOffsetBatchAligned());
        } else if (log.isTraceEnabled()) {
            log.trace(skipReason);
        }
    }
}
