public class kafka_0084 {

        @Override
        public boolean assignRefactored(final Map<ProcessId, ClientState> clients,
                              final Set<TaskId> allTaskIds,
                              final Set<TaskId> statefulTaskIds,
                              final AssignmentConfigs configs) {
            final int numStandbyReplicas = configs.numStandbyReplicas();
            final Map<TaskId, Integer> tasksToRemainingStandbys = computeTasksToRemainingStandbys(numStandbyReplicas,
                                                                                                  statefulTaskIds);
    
            final ConstrainedPrioritySet standbyTaskClientsByTaskLoad = createLeastLoadedPrioritySetConstrainedByAssignedTask(clients);
    
            standbyTaskClientsByTaskLoad.offerAll(clients.keySet());
    
            for (final TaskId task : statefulTaskIds) {
                pollClientAndMaybeAssignAndUpdateRemainingStandbyTasks(numStandbyReplicas,
                                                                       clients,
                                                                       tasksToRemainingStandbys,
                                                                       standbyTaskClientsByTaskLoad,
                                                                       task,
                                                                       log);
            }
    
            // returning false, because standby task assignment will never require a follow-up probing rebalance.
            return false;
        }
}
