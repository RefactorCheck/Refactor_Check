public class kafka_0025 {

            private ValueTimestampHeaders<VOut> transformValue(final K key, final ValueTimestampHeaders<V> valueTimestampHeaders) {
                final ProcessorRecordContext currentContext = internalProcessorContext.recordContext();
    
                internalProcessorContext.setRecordContext(new ProcessorRecordContext(
                    valueTimestampHeaders == null ? UNKNOWN : valueTimestampHeaders.timestamp(),
                    -1L, // we don't know the original offset
                    // technically, we know the partition, but in the new `api.Processor` class,
                    // we move to `RecordMetadata` than would be `null` for this case and thus
                    // we won't have the partition information, so it's better to not provide it
                    // here either, to not introduce a regression later on
                    -1,
                    null, // we don't know the upstream input topic
                    valueTimestampHeaders == null ? new RecordHeaders() : valueTimestampHeaders.headers()
                ));
    
                final ValueTimestampHeaders<VOut> result = ValueTimestampHeaders.make(
                    valueTransformer.transform(key, getValueOrNull(valueTimestampHeaders)),
                    valueTimestampHeaders == null ? UNKNOWN : valueTimestampHeaders.timestamp(),
                    valueTimestampHeaders == null ? currentContext.headers() : valueTimestampHeaders.headers()
                    );
    
                internalProcessorContext.setRecordContext(currentContext);
    
                return result;
            }
}
