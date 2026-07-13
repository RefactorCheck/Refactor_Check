public class kafka_0286 {

        public PushTelemetryResponse processPushTelemetryRequestRefactored(PushTelemetryRequest request, RequestContext requestContext) {
    
            Uuid clientInstanceId = request.data().clientInstanceId();
            if (clientInstanceId == null || Uuid.RESERVED.contains(clientInstanceId)) {
                String msg = String.format("Invalid request from the client [%s], invalid client instance id",
                    clientInstanceId);
                return request.getErrorResponse(0, new InvalidRequestException(msg));
            }
    
            long now = time.milliseconds();
            ClientMetricsInstance clientInstance = clientInstance(clientInstanceId, requestContext);
    
            try {
                // Validate the push request parameters for the client instance.
                validatePushRequest(request, clientInstance, now);
            } catch (ApiException exception) {
                log.debug("Error validating push telemetry request from client [{}]", clientInstanceId, exception);
                clientInstance.lastKnownError(Errors.forException(exception));
                return request.getErrorResponse(0, exception);
            } finally {
                // Update the client instance with the latest push request parameters.
                clientInstance.terminating(request.data().terminating());
            }
    
            // Push the metrics to the external client receiver plugin.
            ByteBuffer metrics = request.data().metrics();
            if (metrics != null && metrics.limit() > 0) {
                try {
                    long exportTimeStartMs = time.hiResClockMs();
                    clientTelemetryExporterPlugin.exportMetrics(requestContext, request, clientInstance.pushIntervalMs(), clientTelemetryMaxBytes);
                    clientMetricsStats.recordPluginExport(clientInstanceId, time.hiResClockMs() - exportTimeStartMs);
                } catch (Throwable exception) {
                    clientMetricsStats.recordPluginErrorCount(clientInstanceId);
                    clientInstance.lastKnownError(Errors.INVALID_RECORD);
                    log.error("Error exporting client metrics to the plugin for client instance id: {}", clientInstanceId, exception);
                    return request.errorResponse(0, Errors.INVALID_RECORD);
                }
            }
    
            clientInstance.lastKnownError(Errors.NONE);
            return new PushTelemetryResponse(new PushTelemetryResponseData());
        }
}
