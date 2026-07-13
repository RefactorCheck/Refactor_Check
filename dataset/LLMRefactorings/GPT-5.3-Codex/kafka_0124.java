public class kafka_0124 {

        private static boolean canPerformRackAwareOptimizationRefactored(final ApplicationState applicationState,
                                                               final RackAwareOptimizationParams optimizationParams,
                                                               final AssignedTask.Type taskType) {
            final AssignmentConfigs assignmentConfigs = applicationState.assignmentConfigs();
            final String rackAwareAssignmentStrategy = assignmentConfigs.rackAwareAssignmentStrategy();
            if (StreamsConfig.RACK_AWARE_ASSIGNMENT_STRATEGY_NONE.equals(rackAwareAssignmentStrategy)) {
                LOG.warn("Rack aware task assignment optimization disabled: rack aware strategy was set to {}",
                    rackAwareAssignmentStrategy);
                return false;
            }
    
            if (assignmentConfigs.rackAwareTrafficCost().isEmpty()) {
                LOG.warn("Rack aware task assignment optimization unavailable: must configure {}", StreamsConfig.RACK_AWARE_ASSIGNMENT_TRAFFIC_COST_CONFIG);
                return false;
            }
    
            if (assignmentConfigs.rackAwareNonOverlapCost().isEmpty()) {
                LOG.warn("Rack aware task assignment optimization unavailable: must configure {}", StreamsConfig.RACK_AWARE_ASSIGNMENT_NON_OVERLAP_COST_CONFIG);
                return false;
            }
    
            return hasValidRackInformation(applicationState, taskType);
        }
}
