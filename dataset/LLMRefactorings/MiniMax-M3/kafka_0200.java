public class kafka_0200 {

        void populateContextHeaders(ProducerRecord<byte[], byte[]> producerRecord, ProcessingContext<ConsumerRecord<byte[], byte[]>> context) {
            Headers headers = producerRecord.headers();
            addOriginalHeaders(headers, context);
            headers.add(ERROR_HEADER_CONNECTOR_NAME, toBytes(connectorTaskId.connector()));
            headers.add(ERROR_HEADER_TASK_ID, toBytes(String.valueOf(connectorTaskId.task())));
            headers.add(ERROR_HEADER_STAGE, toBytes(context.stage().name()));
            headers.add(ERROR_HEADER_EXECUTING_CLASS, toBytes(context.executingClass().getName()));
            addErrorHeaders(headers, context);
        }

        private void addOriginalHeaders(Headers headers, ProcessingContext<ConsumerRecord<byte[], byte[]>> context) {
            if (context.original() != null) {
                headers.add(ERROR_HEADER_ORIG_TOPIC, toBytes(context.original().topic()));
                headers.add(ERROR_HEADER_ORIG_PARTITION, toBytes(context.original().partition()));
                headers.add(ERROR_HEADER_ORIG_OFFSET, toBytes(context.original().offset()));
            }
        }

        private void addErrorHeaders(Headers headers, ProcessingContext<ConsumerRecord<byte[], byte[]>> context) {
            if (context.error() != null) {
                headers.add(ERROR_HEADER_EXCEPTION, toBytes(context.error().getClass().getName()));
                headers.add(ERROR_HEADER_EXCEPTION_MESSAGE, toBytes(context.error().getMessage()));
                byte[] trace;
                if ((trace = stacktrace(context.error())) != null) {
                    headers.add(ERROR_HEADER_EXCEPTION_STACK_TRACE, trace);
                }
            }
        }
}
