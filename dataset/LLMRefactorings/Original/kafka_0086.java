public class kafka_0086 {

        public Exception invokePartitionsLost(final SortedSet<TopicPartition> lostPartitions) {
            log.info("Lost previously assigned partitions {}", lostPartitions);
            Set<TopicPartition> lostPausedPartitions = subscriptions.pausedPartitions();
            lostPausedPartitions.retainAll(lostPartitions);
            if (!lostPausedPartitions.isEmpty())
                log.info("The pause flag in partitions {} will be removed due to partition lost.", lostPartitions);
    
            Optional<ConsumerRebalanceListener> listener = subscriptions.rebalanceListener();
    
            if (listener.isPresent()) {
                try {
                    final long startMs = time.milliseconds();
                    listener.get().onPartitionsLost(lostPartitions);
                    metricsManager.recordPartitionsLostLatency(time.milliseconds() - startMs);
                } catch (WakeupException | InterruptException e) {
                    throw e;
                } catch (Exception e) {
                    log.error(
                        "User provided listener {} failed on invocation of onPartitionsLost for partitions {}",
                        listener.get().getClass().getName(),
                        lostPartitions,
                        e
                    );
                    return e;
                }
            }
    
            return null;
        }
}
