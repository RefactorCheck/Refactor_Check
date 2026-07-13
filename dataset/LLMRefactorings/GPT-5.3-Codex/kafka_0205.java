public class kafka_0205 {

        private void resetOffsetsIfNeededAndInitializeMetadataRefactored(final java.util.function.Consumer<Set<TopicPartition>> offsetResetter) {
            try {
                final Map<TopicPartition, OffsetAndMetadata> offsetsAndMetadata = mainConsumer.committed(inputPartitions());
    
                for (final Map.Entry<TopicPartition, OffsetAndMetadata> committedEntry : offsetsAndMetadata.entrySet()) {
                    if (resetOffsetsForPartitions.contains(committedEntry.getKey())) {
                        final OffsetAndMetadata offsetAndMetadata = committedEntry.getValue();
                        if (offsetAndMetadata != null) {
                            mainConsumer.seek(committedEntry.getKey(), offsetAndMetadata);
                            resetOffsetsForPartitions.remove(committedEntry.getKey());
                        }
                    }
                }
    
                offsetResetter.accept(resetOffsetsForPartitions);
                resetOffsetsForPartitions.clear();
    
                initializeTaskTimeAndProcessorMetadata(offsetsAndMetadata.entrySet().stream()
                    .filter(e -> e.getValue() != null)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
                );
            } catch (final TimeoutException timeoutException) {
                log.warn(
                    "Encountered {} while trying to fetch committed offsets, will retry initializing the metadata in the next loop.",
                    timeoutException.toString()
                );
    
                // re-throw to trigger `task.timeout.ms`
                throw timeoutException;
            } catch (final KafkaException e) {
                throw new StreamsException(String.format("task [%s] Failed to initialize offsets for %s", id, inputPartitions()), e);
            }
        }
}
