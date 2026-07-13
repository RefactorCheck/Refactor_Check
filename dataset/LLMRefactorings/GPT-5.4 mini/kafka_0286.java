public class kafka_0286 {

        public PushTelemetryResponse processPushTelemetryRequest(PushTelemetryRequest request, RequestContext requestContext) {

            Uuid clientInstanceId = request.data().clientInstanceId();
            if (clientInstanceId == null || Uuid.RESERVED.contains(clientInstanceId)) {
                String msg = String.format("Invalid request from the client [%s], invalid client instance id",
                    clientInstanceId);
                return request.getErrorResponse(0, new InvalidRequestException(msg));
            }

            long now = time.milliseconds();
            ClientMetricsInstance clientInstance = clientInstance(clientInstanceId, requestContext);

            try {
                validatePushRequest(request, clientInstance, now);
            } catch (ApiException exception) {
                log.debug("Error validating push telemetry request from client [{}]", clientInstanceId, exception);
                clientInstance.lastKnownError(Errors.forException(exception));
                return request.getErrorResponse(0, exception);
            } finally {
                clientInstance.terminating(request.data().terminating());
            }

            return exportMetrics(request, requestContext, clientInstance, clientInstanceId);
        }

        private PushTelemetryResponse exportMetrics(PushTelemetryRequest request, RequestContext requestContext, ClientMetricsInstance clientInstance, Uuid clientInstanceId) {
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
