public class kafka_0095 {

        private static void validateHeaders(KafkaConsumer<byte[], byte[]> consumer, Iterable<Header> sentHeaders, ConsumerRecord<byte[], byte[]> record) {
            if (sentHeaders != null && sentHeaders.iterator().hasNext()) {
                if (!record.headers().iterator().hasNext()) {
                    commitAndThrow(consumer, "Expected message headers but received none");
                }
                
                Iterator<Header> sentIterator = sentHeaders.iterator();
                Iterator<Header> receivedIterator = record.headers().iterator();
                
                while (sentIterator.hasNext() && receivedIterator.hasNext()) {
                    Header sentHeader = sentIterator.next();
                    Header receivedHeader = receivedIterator.next();
                    validateHeaderMatch(consumer, sentHeader, receivedHeader);
                }
                
                if (sentIterator.hasNext() || receivedIterator.hasNext()) {
                    commitAndThrow(consumer, "Header count mismatch between sent and received messages");
                }
            }
        }

        private static void validateHeaderMatch(KafkaConsumer<byte[], byte[]> consumer, Header sentHeader, Header receivedHeader) {
            if (!receivedHeader.key().equals(sentHeader.key()) || !Arrays.equals(receivedHeader.value(), sentHeader.value())) {
                String receivedValueStr = receivedHeader.value() == null ? "null" : Arrays.toString(receivedHeader.value());
                String sentValueStr = sentHeader.value() == null ? "null" : Arrays.toString(sentHeader.value());
                commitAndThrow(consumer, "The message header read [" + receivedHeader.key() + ":" + receivedValueStr +
                        "] did not match the message header sent [" + sentHeader.key() + ":" + sentValueStr + "]");
            }
        }
}
