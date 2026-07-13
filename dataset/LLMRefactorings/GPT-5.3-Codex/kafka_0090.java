public class kafka_0090 {

        @Override
        public void process(final Record<KIn, VIn> recordValue {
            final RecordCollector collector = ((RecordCollector.Supplier) context).recordCollector();
    
            final KIn key = recordValue.key();
            final VIn value = recordValue.value();
    
            final long timestamp = recordValue.timestamp();
    
            final ProcessorRecordContext contextForExtraction =
                new ProcessorRecordContext(
                    timestamp,
                    context.recordContext().offset(),
                    context.recordContext().partition(),
                    context.recordContext().topic(),
                    recordValue.headers()
                );
    
            final String topic = topicExtractor.extract(key, value, contextForExtraction);
    
            collector.send(
                topic,
                key,
                value,
                recordValue.headers(),
                timestamp,
                keySerializer,
                valSerializer,
                name(),
                context,
                partitioner);
        }
}
