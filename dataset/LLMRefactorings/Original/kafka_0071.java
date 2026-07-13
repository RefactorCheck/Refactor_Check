public class kafka_0071 {

        public static void run(ConsoleShareConsumerOptions opts) {
            messageCount = 0;
            long timeoutMs = opts.timeoutMs() >= 0 ? opts.timeoutMs() : Long.MAX_VALUE;
    
            Properties consumerProps = opts.consumerProps();
            // Set share acknowledgement mode to explicit.
            consumerProps.put(ConsumerConfig.SHARE_ACKNOWLEDGEMENT_MODE_CONFIG, "explicit");
    
            ShareConsumer<byte[], byte[]> consumer = new KafkaShareConsumer<>(consumerProps, new ByteArrayDeserializer(), new ByteArrayDeserializer());
            ConsumerWrapper consumerWrapper = new ConsumerWrapper(opts.topicArg(), consumer, timeoutMs);
    
            addShutdownHook(consumerWrapper, opts);
    
            try {
                process(opts.maxMessages(), opts.formatter(), consumerWrapper, System.out, opts.rejectMessageOnError(), opts.acknowledgeType());
            } finally {
                consumerWrapper.cleanup();
                opts.formatter().close();
                reportRecordCount();
    
                SHUTDOWN_LATCH.countDown();
            }
        }
}
