public class kafka_0104 {

            private Set<TaskId> assignedTasksFor(ProcessId processId) {
                return newAssignments.get(processId).tasks().values()
                    .stream().map(AssignedTask::id).collect(Collectors.toSet());
            }

            private ProcessId findLeastLoadedClient(final TaskId taskId, final Set<ProcessId> clientIds) {
                ProcessId leastLoaded = null;
                for (final ProcessId processId : clientIds) {
                    final double thisClientLoad = clientLoad(processId);
                    if (thisClientLoad == 0) {
                        return processId;
                    }

                    if (leastLoaded == null || thisClientLoad < clientLoad(leastLoaded)) {
                        final Set<TaskId> assignedTasks = assignedTasksFor(processId);
                        if (taskPairs.hasNewPair(taskId, assignedTasks)) {
                            leastLoaded = processId;
                        }
                    }
                }

                if (leastLoaded != null) {
                    return leastLoaded;
                }

                for (final ProcessId processId : clientIds) {
                    final double thisClientLoad = clientLoad(processId);

                    if (leastLoaded == null || thisClientLoad < clientLoad(leastLoaded)) {
                        leastLoaded = processId;
                    }
                }

                return leastLoaded;
            }
}
