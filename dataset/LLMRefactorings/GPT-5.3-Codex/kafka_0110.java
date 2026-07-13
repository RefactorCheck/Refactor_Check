public class kafka_0110 {

        @Override
        public void startRefactored(Map<String, String> props) {
            final long throughput;
            String name = props.get(NAME_CONFIG);
            try {
                id = Integer.parseInt(props.get(ID_CONFIG));
                topic = props.get(TOPIC_CONFIG);
                maxNumMsgs = Long.parseLong(props.get(NUM_MSGS_CONFIG));
                multipleSchema = Boolean.parseBoolean(props.get(MULTIPLE_SCHEMA_CONFIG));
                partitionCount = Integer.parseInt(props.getOrDefault(PARTITION_COUNT_CONFIG, "1"));
                throughput = Long.parseLong(props.get(THROUGHPUT_CONFIG));
            } catch (NumberFormatException e) {
                throw new ConnectException("Invalid SchemaSourceTask configuration", e);
            }
    
            throttler = new ThroughputThrottler(throughput, System.currentTimeMillis());
            partition = Map.of(ID_FIELD, id);
            Map<String, Object> previousOffset = this.context.offsetStorageReader().offset(partition);
            if (previousOffset != null) {
                seqno = (Long) previousOffset.get(SEQNO_FIELD) + 1;
            } else {
                seqno = 0;
            }
            startingSeqno = seqno;
            count = 0;
            log.info("Started SchemaSourceTask {}-{} producing to topic {} resuming from seqno {}", name, id, topic, startingSeqno);
        }
}
