public class kafka_0053 {

        static void execute(Admin adminClient, String offsetJsonString, PrintStream out) throws JsonProcessingException {
            Map<TopicPartition, List<Long>> offsetSeq = parseOffsetJsonStringWithoutDedup(offsetJsonString);
    
            Set<TopicPartition> duplicatePartitions = offsetSeq.entrySet().stream()
                .filter(e -> e.getValue().size() > 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    
            if (!duplicatePartitions.isEmpty()) {
                StringJoiner duplicates = new StringJoiner(",");
                duplicatePartitions.forEach(tp -> duplicates.add(tp.toString()));
                throw new AdminCommandFailedException(
                    String.format("Offset json file contains duplicate topic partitions: %s", duplicates)
                );
            }
    
            Map<TopicPartition, RecordsToDelete> recordsToDelete = new HashMap<>();
    
            for (Map.Entry<TopicPartition, List<Long>> e : offsetSeq.entrySet())
                recordsToDelete.put(e.getKey(), RecordsToDelete.beforeOffset(e.getValue().get(0)));
    
            out.println("Executing records delete operation");
            DeleteRecordsResult deleteRecordsResult = adminClient.deleteRecords(recordsToDelete);
            out.println("Records delete operation completed:");
    
            printDeleteRecordsResult(deleteRecordsResult, out);
        }

        private static void printDeleteRecordsResult(DeleteRecordsResult deleteRecordsResult, PrintStream out) {
            deleteRecordsResult.lowWatermarks().forEach((tp, partitionResult) -> {
                try {
                    out.printf("partition: %s\tlow_watermark: %s%n", tp, partitionResult.get().lowWatermark());
                } catch (InterruptedException | ExecutionException e) {
                    out.printf("partition: %s\terror: %s%n", tp, e.getMessage());
                }
            });
        }
}
