public class kafka_0155 {

        @Override
        public void start(Map<String, String> props) {
            final long throughput;
            try {
                name = props.get(NAME_CONFIG);
                id = Integer.parseInt(props.get(ID_CONFIG));
                topic = props.get(TOPIC_CONFIG);
                throughput = Long.parseLong(props.get(THROUGHPUT_CONFIG));
            } catch (NumberFormatException e) {
                throw new ConnectException("Invalid VerifiableSourceTask configuration", e);
            }
    
            partition = Map.of(ID_FIELD, id);
            initializeSeqno();
            startingSeqno = seqno;
            throttler = new ThroughputThrottler(throughput, System.currentTimeMillis());
            completeRecordData = "true".equalsIgnoreCase(props.get(COMPLETE_RECORD_DATA_CONFIG));
    
            log.info("Started VerifiableSourceTask {}-{} producing to topic {} resuming from seqno {}", name, id, topic, startingSeqno);
        }

        private void initializeSeqno() {
            Map<String, Object> previousOffset = this.context.offsetStorageReader().offset(partition);
            if (previousOffset != null)
                seqno = (Long) previousOffset.get(SEQNO_FIELD) + 1;
            else
                seqno = 0;
        }
}
