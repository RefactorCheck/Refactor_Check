public class kafka_0124 {

        private static boolean canPerformRackAwareOptimization(final ApplicationState applicationState,
                                                               final RackAwareOptimizationParams optimizationParams,
                                                               final AssignedTask.Type taskType) {
            final AssignmentConfigs assignmentConfigs = applicationState.assignmentConfigs();
            final String rackAwareAssignmentStrategy = assignmentConfigs.rackAwareAssignmentStrategy();
            if (StreamsConfig.RACK_AWARE_ASSIGNMENT_STRATEGY_NONE.equals(rackAwareAssignmentStrategy)) {
                LOG.warn("Rack aware task assignment optimization disabled: rack aware strategy was set to {}",
                    rackAwareAssignmentStrategy);
                return false;
            }

            if (isRequiredConfigMissing(assignmentConfigs.rackAwareTrafficCost(), StreamsConfig.RACK_AWARE_ASSIGNMENT_TRAFFIC_COST_CONFIG)) {
                return false;
            }

            if (isRequiredConfigMissing(assignmentConfigs.rackAwareNonOverlapCost(), StreamsConfig.RACK_AWARE_ASSIGNMENT_NON_OVERLAP_COST_CONFIG)) {
                return false;
            }

            return hasValidRackInformation(applicationState, taskType);
        }

        private static boolean isRequiredConfigMissing(final Optional<?> configValue, final String configName) {
            if (configValue.isEmpty()) {
                LOG.warn("Rack aware task assignment optimization unavailable: must configure {}", configName);
                return true;
            }
            return false;
        }
}
