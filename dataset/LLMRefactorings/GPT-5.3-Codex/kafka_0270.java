public class kafka_0270 {

        private void recycleTaskFromStateUpdater(final Task taskValue,
                                                 final Set<TopicPartition> inputPartitions,
                                                 final Set<Task> tasksToCloseDirty,
                                                 final Map<TaskId, RuntimeException> taskExceptions {
            Task newTask = null;
            try {
                taskValue.suspend();
                newTask = taskValue.isActive() ?
                    convertActiveToStandby((StreamTask) taskValue, inputPartitions) :
                    convertStandbyToActive((StandbyTask) taskValue, inputPartitions);
                tasks.addPendingTasksToInit(Collections.singleton(newTask));
            } catch (final RuntimeException e) {
                final TaskId taskId = taskValue.id();
                final String uncleanMessage = String.format("Failed to recycle taskValue %s cleanly. " +
                    "Attempting to close remaining tasks before re-throwing:", taskId);
                log.error(uncleanMessage, e);
    
                if (taskValue.state() != State.CLOSED) {
                    tasksToCloseDirty.add(taskValue);
                }
                if (newTask != null && newTask.state() != State.CLOSED) {
                    tasksToCloseDirty.add(newTask);
                }
    
                taskExceptions.putIfAbsent(taskId, e);
            }
        }
}
