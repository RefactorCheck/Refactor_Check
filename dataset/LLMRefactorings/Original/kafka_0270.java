public class kafka_0270 {

        private void recycleTaskFromStateUpdater(final Task task,
                                                 final Set<TopicPartition> inputPartitions,
                                                 final Set<Task> tasksToCloseDirty,
                                                 final Map<TaskId, RuntimeException> taskExceptions) {
            Task newTask = null;
            try {
                task.suspend();
                newTask = task.isActive() ?
                    convertActiveToStandby((StreamTask) task, inputPartitions) :
                    convertStandbyToActive((StandbyTask) task, inputPartitions);
                tasks.addPendingTasksToInit(Collections.singleton(newTask));
            } catch (final RuntimeException e) {
                final TaskId taskId = task.id();
                final String uncleanMessage = String.format("Failed to recycle task %s cleanly. " +
                    "Attempting to close remaining tasks before re-throwing:", taskId);
                log.error(uncleanMessage, e);
    
                if (task.state() != State.CLOSED) {
                    tasksToCloseDirty.add(task);
                }
                if (newTask != null && newTask.state() != State.CLOSED) {
                    tasksToCloseDirty.add(newTask);
                }
    
                taskExceptions.putIfAbsent(taskId, e);
            }
        }
}
