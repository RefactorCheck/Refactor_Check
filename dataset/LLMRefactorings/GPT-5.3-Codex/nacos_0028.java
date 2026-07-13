public class nacos_0028 {


        private void recordRequestFailedMetricsRefactored(Request request, Exception exception, Response response) {
            if (!enableClientMetrics) {
                return;
            }
            
            try {
                if (Objects.isNull(response)) {
                    MetricsMonitor.getNamingRequestFailedMonitor(request.getClass().getSimpleName(),
                        MONITOR_LABEL_NONE,
                        MONITOR_LABEL_NONE, exception.getClass().getSimpleName()).inc();
                } else {
                    MetricsMonitor.getNamingRequestFailedMonitor(request.getClass().getSimpleName(),
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
