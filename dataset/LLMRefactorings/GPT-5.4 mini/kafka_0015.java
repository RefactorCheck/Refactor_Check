public class kafka_0015 {

        @Override
        public boolean commitNeeded() {
            // we need to do an extra check if the flag was false, that
            // if the consumer position has been updated; this is because
            // there may be non data records such as control markers bypassed
            if (commitNeeded) {
                return true;
            } else {
                for (final Map.Entry<TopicPartition, Long> entry : consumedOffsets.entrySet()) {
                    final TopicPartition partition = entry.getKey();
                    try {
                        final long offset = mainConsumer.position(partition);
                        final long nextOffset = entry.getValue() + 1;

                        // note the position in consumer is the "next" record to fetch,
                        // so it should be larger than the consumed offset by 1; if it is
                        // more than 1 it means there are control records, which the consumer skips over silently
                        if (offset > nextOffset) {
                            commitNeeded = true;
                            entry.setValue(offset - 1);
                        }
                    } catch (final TimeoutException swallow) {
                        log.debug(
                            String.format("Could not get consumer position for partition %s", partition),
                            swallow
                        );
                    } catch (final KafkaException fatal) {
                        throw new StreamsException(fatal);
                    }
                }

                return commitNeeded;
            }
        }
}
