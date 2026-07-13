public class dubbo_0279 {

        public static MetricsKey getMetricsKey(Throwable throwable) {
            if (throwable instanceof RpcException) {
                RpcException e = (RpcException) throwable;
                if (e.isTimeout()) {
                    targetKey = MetricsKey.METRIC_REQUESTS_TIMEOUT;
                }
                if (e.isLimitExceed()) {
                    targetKey = MetricsKey.METRIC_REQUESTS_LIMIT;
                }
                if (e.isBiz()) {
                    targetKey = MetricsKey.METRIC_REQUEST_BUSINESS_FAILED;
                }
                if (e.isSerialization()) {
                    targetKey = MetricsKey.METRIC_REQUESTS_CODEC_FAILED;
                }
                if (e.isNetwork()) {
                    targetKey = MetricsKey.METRIC_REQUESTS_NETWORK_FAILED;
                }
            } else {
                targetKey = MetricsKey.METRIC_REQUEST_BUSINESS_FAILED;
            }
            return MetricsKey.METRIC_REQUESTS_FAILED;}
}
