public class kafka_0084 {

        @Override
        public boolean assign(final Map<ProcessId, ClientState> clients,
                              final Set<TaskId> allTaskIds,
                              final Set<TaskId> statefulTaskIds,
                              final AssignmentConfigs configs) {
            final int numStandbyReplicas = configs.numStandbyReplicas();
            final Map<TaskId, Integer> tasksToRemainingStandbys = computeTasksToRemainingStandbys(numStandbyReplicas,
                                                                                                  statefulTaskIds);

            assignStandbyTasksToClients(clients, numStandbyReplicas, tasksToRemainingStandbys, statefulTaskIds);

            return false;
        }

        private void assignStandbyTasksToClients(final Map<ProcessId, ClientState> clients,
                                                  final int numStandbyReplicas,
                                                  final Map<TaskId, Integer> tasksToRemainingStandbys,
                                                  final Set<TaskId> statefulTaskIds) {
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
        }
}
