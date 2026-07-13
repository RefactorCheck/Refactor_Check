public class kafka_0084 {

        @Override
        public boolean assign(final Map<ProcessId, ClientState> clients,
                              final Set<TaskId> allTaskIds,
                              final Set<TaskId> statefulTaskIds,
                              final AssignmentConfigs configs) {
            final int numStandbyReplicas = configs.numStandbyReplicas();
            final Map<TaskId, Integer> tasksToRemainingStandbys = computeTasksToRemainingStandbys(numStandbyReplicas,
                                                                                                  statefulTaskIds);

            final ConstrainedPrioritySet leastLoadedStandbyTaskClients = createLeastLoadedPrioritySetConstrainedByAssignedTask(clients);

            leastLoadedStandbyTaskClients.offerAll(clients.keySet());

            for (final TaskId task : statefulTaskIds) {
                pollClientAndMaybeAssignAndUpdateRemainingStandbyTasks(numStandbyReplicas,
                                                                       clients,
                                                                       tasksToRemainingStandbys,
                                                                       leastLoadedStandbyTaskClients,
                                                                       task,
                                                                       log);
            }

            return false;
        }
}
