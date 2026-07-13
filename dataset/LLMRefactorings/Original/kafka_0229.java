public class kafka_0229 {

        public OptionalLong translateDownstream(String group, TopicPartition sourceTopicPartition, long upstreamOffset) {
            if (!readToEnd) {
                // If we have not read to the end of the syncs topic at least once, decline to translate any offsets.
                // This prevents emitting stale offsets while initially reading the offset syncs topic.
                log.debug("translateDownstream({},{},{}): Skipped (initial offset syncs read still in progress)",
                        group, sourceTopicPartition, upstreamOffset);
                return OptionalLong.empty();
            }
            Optional<OffsetSync> offsetSync = latestOffsetSync(sourceTopicPartition, upstreamOffset);
            if (offsetSync.isPresent()) {
                if (offsetSync.get().upstreamOffset() > upstreamOffset) {
                    // Offset is too far in the past to translate accurately
                    log.debug("translateDownstream({},{},{}): Skipped ({} is ahead of upstream consumer group {})",
                            group, sourceTopicPartition, upstreamOffset,
                            offsetSync.get(), upstreamOffset);
                    return OptionalLong.of(-1L);
                }
                // If the consumer group is ahead of the offset sync, we can translate the upstream offset only 1
                // downstream offset past the offset sync itself. This is because we know that future records must appear
                // ahead of the offset sync, but we cannot estimate how many offsets from the upstream topic
                // will be written vs dropped. If we overestimate, then we may skip the correct offset and have data loss.
                // This also handles consumer groups at the end of a topic whose offsets point past the last valid record.
                // This may cause re-reading of records depending on the age of the offset sync.
                // s=offset sync pair, ?=record may or may not be replicated, g=consumer group offset, r=re-read record
                // source |-s?????r???g-|
                //          |  ______/
                //          | /
                //          vv
                // target |-sg----r-----|
                long translatedDownstreamOffset = offsetSync.get().translateDownstream(upstreamOffset);
                log.debug("translateDownstream({},{},{}): Translated {} (relative to {})",
                        group, sourceTopicPartition, upstreamOffset,
                        translatedDownstreamOffset,
                        offsetSync.get()
                );
                return OptionalLong.of(translatedDownstreamOffset);
            } else {
                log.debug("translateDownstream({},{},{}): Skipped (offset sync not found)",
                        group, sourceTopicPartition, upstreamOffset);
                return OptionalLong.empty();
            }
        }
}
