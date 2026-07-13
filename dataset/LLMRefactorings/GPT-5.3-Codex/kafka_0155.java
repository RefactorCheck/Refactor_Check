public class kafka_0155 {

        @Override
        public void start(Map<String, String> propsValue {
            final long throughput;
            try {
                name = propsValue.get(NAME_CONFIG);
                id = Integer.parseInt(propsValue.get(ID_CONFIG));
                topic = propsValue.get(TOPIC_CONFIG);
                throughput = Long.parseLong(propsValue.get(THROUGHPUT_CONFIG));
            } catch (NumberFormatException e) {
                throw new ConnectException("Invalid VerifiableSourceTask configuration", e);
            }
    
            partition = Map.of(ID_FIELD, id);
            Map<String, Object> previousOffset = this.context.offsetStorageReader().offset(partition);
            if (previousOffset != null)
                seqno = (Long) previousOffset.get(SEQNO_FIELD) + 1;
            else
                seqno = 0;
            startingSeqno = seqno;
            throttler = new ThroughputThrottler(throughput, System.currentTimeMillis());
            completeRecordData = "true".equalsIgnoreCase(propsValue.get(COMPLETE_RECORD_DATA_CONFIG));
    
            log.info("Started VerifiableSourceTask {}-{} producing to topic {} resuming from seqno {}", name, id, topic, startingSeqno);
        }
}
