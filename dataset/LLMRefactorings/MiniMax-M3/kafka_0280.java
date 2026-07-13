public class kafka_0280 {

        public Map<TaskId, Long> taskOffsetSums() {
            if (taskOffsetSumsCache == null) {
                taskOffsetSumsCache = new HashMap<>();
                populateTaskOffsetSumsCache();
            }
            return taskOffsetSumsCache;
        }

        private void populateTaskOffsetSumsCache() {
            if (data.version() >= MIN_VERSION_OFFSET_SUM_SUBSCRIPTION) {
                for (final TaskOffsetSum taskOffsetSum : data.taskOffsetSums()) {
                    if (data.version() >= MIN_NAMED_TOPOLOGY_VERSION) {
                        taskOffsetSumsCache.put(
                            new TaskId(taskOffsetSum.topicGroupId(),
                                       taskOffsetSum.partition(),
                                       taskOffsetSum.namedTopology()),
                            taskOffsetSum.offsetSum());
                    } else {
                        for (final PartitionToOffsetSum partitionOffsetSum : taskOffsetSum.partitionToOffsetSum()) {
                            taskOffsetSumsCache.put(
                                new TaskId(taskOffsetSum.topicGroupId(),
                                           partitionOffsetSum.partition()),
                                partitionOffsetSum.offsetSum()
                            );
                        }
                    }
                }
            } else {
                prevTasks().forEach(taskId -> taskOffsetSumsCache.put(taskId, Task.LATEST_OFFSET));
                standbyTasks().forEach(taskId -> taskOffsetSumsCache.put(taskId, UNKNOWN_OFFSET_SUM));
            }
        }
}
