public class kafka_0059 {

        @Override
        public void onComplete(Map<TopicIdPartition, Set<Long>> offsetsMap, Exception exception) {
            List<AcknowledgedData> acknowledgedOffsets = new ArrayList<>();
            int totalAcknowledged = 0;
            for (Map.Entry<TopicIdPartition, Set<Long>> offsetEntry : offsetsMap.entrySet()) {
                TopicIdPartition tp = offsetEntry.getKey();
                final int acknowledgedCount = offsetEntry.getValue().size();
                acknowledgedOffsets.add(new AcknowledgedData(tp.topic(), tp.partition(), offsetEntry.getValue()));
                totalAcknowledged += acknowledgedCount;
            }

            boolean success = true;
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
