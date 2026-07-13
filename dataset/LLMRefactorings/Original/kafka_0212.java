public class kafka_0212 {

        void publishLogDelta(MetadataImage newImage, LogDeltaManifest manifest) {
            bytesSinceLastSnapshot += manifest.numBytes();
            if (bytesSinceLastSnapshot >= maxBytesSinceLastSnapshot) {
                if (eventQueue.isEmpty()) {
                    maybeScheduleEmit("we have replayed at least " + maxBytesSinceLastSnapshot +
                        " bytes", newImage, manifest.provenance().isOffsetBatchAligned());
                } else if (log.isTraceEnabled()) {
                    log.trace("Not scheduling bytes-based snapshot because event queue is not empty yet.");
                }
            } else if (maxTimeSinceLastSnapshotNs != 0 &&
                    (time.nanoseconds() - lastSnapshotTimeNs >= maxTimeSinceLastSnapshotNs)) {
                if (eventQueue.isEmpty()) {
                    maybeScheduleEmit("we have waited at least " +
                        TimeUnit.NANOSECONDS.toMinutes(maxTimeSinceLastSnapshotNs) +
                        " minute(s)", newImage, manifest.provenance().isOffsetBatchAligned());
                } else if (log.isTraceEnabled()) {
                    log.trace("Not scheduling time-based snapshot because event queue is not empty yet.");
                }
            } else if (log.isTraceEnabled()) {
                log.trace("Neither time-based nor bytes-based criteria are met; not scheduling snapshot.");
            }
        }
}
