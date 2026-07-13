public class kafka_0041 {

        public void handleAssignment(final Map<TaskId, Set<TopicPartition>> activeTasks,
                                     final Map<TaskId, Set<TopicPartition>> standbyTasks) {
            log.info("Handle new assignment with:\n" +
                         "\tNew active tasks: {}\n" +
                         "\tNew standby tasks: {}\n" +
                         "\tExisting active tasks: {}\n" +
                         "\tExisting standby tasks: {}",
                     activeTasks.keySet(), standbyTasks.keySet(), activeTaskIds(), standbyTaskIds());
    
            topologyMetadata.addSubscribedTopicsFromAssignment(
                activeTasks.values().stream().flatMap(Collection::stream).collect(Collectors.toSet()),
                logPrefix
            );
    
            final Map<TaskId, Set<TopicPartition>> activeTasksToCreate = new HashMap<>(activeTasks);
            final Map<TaskId, Set<TopicPartition>> standbyTasksToCreate = new HashMap<>(standbyTasks);
            final Map<Task, Set<TopicPartition>> tasksToRecycle = new HashMap<>();
            final Set<Task> tasksToCloseClean = new TreeSet<>(Comparator.comparing(Task::id));
    
            final Set<TaskId> tasksToLock =
                tasks.allInitializedTaskIds().stream()
                    .filter(x -> activeTasksToCreate.containsKey(x) || standbyTasksToCreate.containsKey(x))
                    .collect(Collectors.toSet());
    
            maybeLockTasks(tasksToLock);
    
            // first put aside those unrecognized tasks because of unknown named-topologies
            tasks.clearPendingTasksToCreate();
            tasks.addPendingActiveTasksToCreate(pendingTasksToCreate(activeTasksToCreate));
            tasks.addPendingStandbyTasksToCreate(pendingTasksToCreate(standbyTasksToCreate));
            
            // first rectify all existing tasks:
            // 1. for tasks that are already owned, just update input partitions / resume and skip re-creating them
            // 2. for tasks that have changed active/standby status, just recycle and skip re-creating them
            // 3. otherwise, close them since they are no longer owned
            final Map<TaskId, RuntimeException> failedTasks = new LinkedHashMap<>();
            handleTasks(
                activeTasksToCreate,
                standbyTasksToCreate,
                tasksToRecycle,
                tasksToCloseClean,
                failedTasks
            );
            finalizeAssignment(tasksToRecycle, tasksToCloseClean, tasksToLock, failedTasks);
    
            createNewTasks(activeTasksToCreate, standbyTasksToCreate);
        }

        private void finalizeAssignment(final Map<Task, Set<TopicPartition>> tasksToRecycle,
                                        final Set<Task> tasksToCloseClean,
                                        final Set<TaskId> tasksToLock,
                                        final Map<TaskId, RuntimeException> failedTasks) {
            failedTasks.putAll(collectExceptionsAndFailedTasksFromStateUpdater());
    
            final Map<TaskId, RuntimeException> taskCloseExceptions = closeAndRecycleTasks(tasksToRecycle, tasksToCloseClean);
    
            maybeUnlockTasks(tasksToLock);
    
            failedTasks.putAll(taskCloseExceptions);
            maybeThrowTaskExceptions(failedTasks);
        }
}
