public class nacos_0028 {

        private void recordRequestFailedMetrics(Request request, Exception exception,
            Response response) {
            if (!enableClientMetrics) {
                return;
            }
            
            try {
                String requestType = request.getClass().getSimpleName();
                if (Objects.isNull(response)) {
                    MetricsMonitor.getNamingRequestFailedMonitor(requestType,
                        MONITOR_LABEL_NONE,
                        MONITOR_LABEL_NONE, exception.getClass().getSimpleName()).inc();
                } else {
                    MetricsMonitor.getNamingRequestFailedMonitor(requestType,
                        String.valueOf(response.getResultCode()),
                        String.valueOf(response.getErrorCode()),
                        MONITOR_LABEL_NONE).inc();
                }
            } catch (Throwable t) {
                NAMING_LOGGER.warn("Fail to record metrics for request {}",
                    request.getClass().getSimpleName(), t);
            }
        }
}
