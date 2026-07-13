public class kafka_0280 {

        public Map<TaskId, Long> taskOffsetSums() {
            if (taskOffsetSumsCache == null) {
                taskOffsetSumsCache = new HashMap<>();
                final int version = data.version();
                if (version >= MIN_VERSION_OFFSET_SUM_SUBSCRIPTION) {
                    final boolean hasNamedTopology = version >= MIN_NAMED_TOPOLOGY_VERSION;
                    for (final TaskOffsetSum taskOffsetSum : data.taskOffsetSums()) {
                        if (hasNamedTopology) {
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
            return taskOffsetSumsCache;
        }
}
