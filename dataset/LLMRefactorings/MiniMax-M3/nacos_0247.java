public class nacos_0247 {

    private static final String PULL_TYPE_HTTP = "http";

    private void logPullEvent(String dataId, String group, String tenant, String requestIpApp,
            ConfigQueryChainResponse chainResponse, String clientIp, boolean notify, String tag) {

        String pullEvent = resolvePullEvent(chainResponse, tag);

        ConfigQueryChainResponse.ConfigQueryStatus status = chainResponse.getStatus();

        if (status == ConfigQueryChainResponse.ConfigQueryStatus.CONFIG_QUERY_CONFLICT) {
            ConfigTraceService.logPullEvent(dataId, group, tenant, requestIpApp, -1, pullEvent,
                    ConfigTraceService.PULL_TYPE_CONFLICT, -1, clientIp, notify, PULL_TYPE_HTTP);
        } else if (status == ConfigQueryChainResponse.ConfigQueryStatus.CONFIG_NOT_FOUND
                || chainResponse.getContent() == null) {
            ConfigTraceService.logPullEvent(dataId, group, tenant, requestIpApp, -1, pullEvent,
                    ConfigTraceService.PULL_TYPE_NOTFOUND, -1, clientIp, notify, PULL_TYPE_HTTP);
        } else {
            long delayed = System.currentTimeMillis() - chainResponse.getLastModified();
            ConfigTraceService.logPullEvent(dataId, group, tenant, requestIpApp,
                    chainResponse.getLastModified(),
                    pullEvent, ConfigTraceService.PULL_TYPE_OK, delayed, clientIp, notify, PULL_TYPE_HTTP);
        }
    }
}
