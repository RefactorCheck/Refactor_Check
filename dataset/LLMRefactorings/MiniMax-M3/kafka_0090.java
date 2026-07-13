public class kafka_0090 {

    @Override
    public void process(final Record<KIn, VIn> record) {
        final RecordCollector collector = ((RecordCollector.Supplier) context).recordCollector();

        final KIn key = record.key();
        final VIn value = record.value();
        final long timestamp = record.timestamp();

        final ProcessorRecordContext contextForExtraction = createProcessorRecordContext(record, timestamp);
        final String topic = topicExtractor.extract(key, value, contextForExtraction);

        collector.send(
            topic,
            key,
            value,
            record.headers(),
            timestamp,
            keySerializer,
            valSerializer,
            name(),
            context,
            partitioner);
    }

    private ProcessorRecordContext createProcessorRecordContext(final Record<KIn, VIn> record, final long timestamp) {
        return new ProcessorRecordContext(
            timestamp,
            context.recordContext().offset(),
            context.recordContext().partition(),
            context.recordContext().topic(),
            record.headers()
        );
    }
}
