public class kafka_0059 {

        @Override
        public void onComplete(Map<TopicIdPartition, Set<Long>> offsetsMap, Exception exception) {
            final boolean DEFAULT_BOOLEAN_VALUE = true;
            List<AcknowledgedData> acknowledgedOffsets = new ArrayList<>();
            int totalAcknowledged = 0;
            for (Map.Entry<TopicIdPartition, Set<Long>> offsetEntry : offsetsMap.entrySet()) {
                TopicIdPartition tp = offsetEntry.getKey();
                acknowledgedOffsets.add(new AcknowledgedData(tp.topic(), tp.partition(), offsetEntry.getValue()));
                totalAcknowledged += offsetEntry.getValue().size();
            }
    
            boolean success = DEFAULT_BOOLEAN_VALUE;
            String error = null;
            if (exception != null) {
                success = false;
                error = exception.getMessage();
            }
            printJson(new OffsetsAcknowledged(totalAcknowledged, acknowledgedOffsets, error, success));
            if (success) {
                this.totalAcknowledged += totalAcknowledged;
            }
        }
}
