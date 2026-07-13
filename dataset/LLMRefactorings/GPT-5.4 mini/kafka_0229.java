public class kafka_0229 {

        public OptionalLong translateDownstream(String group, TopicPartition sourceTopicPartition, long upstreamOffset) {
            if (!readToEnd) {
                log.debug("translateDownstream({},{},{}): Skipped (initial offset syncs read still in progress)",
                        group, sourceTopicPartition, upstreamOffset);
                return OptionalLong.empty();
            }
            Optional<OffsetSync> offsetSync = latestOffsetSync(sourceTopicPartition, upstreamOffset);
            if (offsetSync.isPresent()) {
                return translateWithOffsetSync(group, sourceTopicPartition, upstreamOffset, offsetSync.get());
            } else {
                log.debug("translateDownstream({},{},{}): Skipped (offset sync not found)",
                        group, sourceTopicPartition, upstreamOffset);
                return OptionalLong.empty();
            }
        }

        private OptionalLong translateWithOffsetSync(String group,
                                                     TopicPartition sourceTopicPartition,
                                                     long upstreamOffset,
                                                     OffsetSync offsetSync) {
            if (offsetSync.upstreamOffset() > upstreamOffset) {
                log.debug("translateDownstream({},{},{}): Skipped ({} is ahead of upstream consumer group {})",
                        group, sourceTopicPartition, upstreamOffset,
                        offsetSync, upstreamOffset);
                return OptionalLong.of(-1L);
            }
            long translatedDownstreamOffset = offsetSync.translateDownstream(upstreamOffset);
            log.debug("translateDownstream({},{},{}): Translated {} (relative to {})",
                    group, sourceTopicPartition, upstreamOffset,
                    translatedDownstreamOffset,
                    offsetSync
            );
            return OptionalLong.of(translatedDownstreamOffset);
        }
}
