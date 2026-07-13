public class kafka_0258 {

        private void close(final Duration timeout, final boolean swallowException) {
            log.trace("Closing the Kafka consumer");
            AtomicReference<Throwable> firstException = new AtomicReference<>();

            wakeupTrigger.disableWakeups();

            final Timer closeTimer = createTimerForCloseRequests(timeout);
            clientTelemetryReporter.ifPresent(ClientTelemetryReporter::initiateClose);
            closeTimer.update();

            swallow(log, Level.ERROR, "Failed to release assignment before closing consumer",
                    () -> sendAcknowledgementsAndLeaveGroup(closeTimer, firstException), firstException);
            swallow(log, Level.ERROR, "Failed to stop finding coordinator",
                    this::stopFindCoordinatorOnClose, firstException);
            swallow(log, Level.ERROR, "Failed invoking acknowledgement commit callback",
                    this::handleCompletedAcknowledgements, firstException);
            swallow(log, Level.ERROR, "Failed processing background events",
                    this::processBackgroundEventsOnClose, firstException);
            if (applicationEventHandler != null)
                closeQuietly(() -> applicationEventHandler.close(Duration.ofMillis(closeTimer.remainingMs())), "Failed shutting down network thread", firstException);
            closeTimer.update();

            if (backgroundEventReaper != null && backgroundEventQueue != null)
                backgroundEventReaper.reap(backgroundEventQueue);

            closeQuietly(kafkaShareConsumerMetrics, "kafka share consumer metrics", firstException);
            closeQuietly(asyncConsumerMetrics, "kafka async consumer metrics", firstException);
            closeQuietly(shareFetchMetricsManager, "kafka share consumer fetch metrics", firstException);
            closeQuietly(metrics, "consumer metrics", firstException);
            closeQuietly(deserializers, "consumer deserializers", firstException);
            clientTelemetryReporter.ifPresent(reporter -> closeQuietly(reporter, "consumer telemetry reporter", firstException));

            AppInfoParser.unregisterAppInfo(CONSUMER_JMX_PREFIX, clientId, metrics);
            log.debug("Kafka share consumer has been closed");
            Throwable exception = firstException.get();
            if (exception != null && !swallowException) {
                if (exception instanceof InterruptException) {
                    throw (InterruptException) exception;
                }
                throw new KafkaException("Failed to close Kafka share consumer", exception);
            }
        }
}
