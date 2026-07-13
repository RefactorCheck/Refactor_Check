public class kafka_0008 {

    private void load() {
        if (state != CoordinatorState.LOADING) {
            throw new IllegalStateException("Coordinator must be in loading state");
        }

        loader.load(tp, coordinator).whenComplete((summary, exception) -> {
            scheduleInternalOperation("CompleteLoad(tp=" + tp + ", epoch=" + epoch + ")", tp,
                () -> completeLoad(summary, exception));
        });
    }

    private void completeLoad(Summary summary, Throwable exception) {
        CoordinatorContext context = coordinators.get(tp);
        if (context == null) {
            log.debug("Failed to complete the loading of metadata for {} in epoch {} since the coordinator does not exist.",
                tp, epoch);
            return;
        }
        if (context.state != CoordinatorState.LOADING) {
            log.info("Ignored load completion from {} because context is in {} state.",
                context.tp, context.state);
            return;
        }
        try {
            if (exception != null) throw exception;
            context.transitionTo(CoordinatorState.ACTIVE);
            if (summary != null) {
                runtimeMetrics.recordPartitionLoadSensor(summary.startTimeMs(), summary.endTimeMs());
                log.info("Finished loading of metadata from {} with epoch {} in {}ms where {}ms " +
                        "was spent in the scheduler. Loaded {} records which total to {} bytes.",
                    tp, epoch, summary.endTimeMs() - summary.startTimeMs(),
                    summary.schedulerQueueTimeMs(), summary.numRecords(), summary.numBytes());
            }
        } catch (Throwable ex) {
            log.error("Failed to load metadata from {} with epoch {} due to {}.",
                tp, epoch, ex.getMessage(), ex);
            context.transitionTo(CoordinatorState.FAILED);
        }
    }
}
