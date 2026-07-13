public class nacos_0149 {

    @Override
    public void run() {
        asyncTimeoutFuture = ConfigExecutor.scheduleLongPolling(this::handleTimeout, timeoutTime, TimeUnit.MILLISECONDS);

        allSubs.add(this);
    }

    private void handleTimeout() {
        try {
            getRetainIps().put(ClientLongPolling.this.ip, System.currentTimeMillis());

            boolean removeFlag = allSubs.remove(ClientLongPolling.this);

            if (removeFlag) {
                LogUtil.CLIENT_LOG.info("{}|{}|{}|{}|{}|{}",
                    (System.currentTimeMillis() - createTime),
                    "timeout",
                    RequestUtil.getRemoteIp((HttpServletRequest) asyncContext.getRequest()),
                    "polling", clientMd5Map.size(), probeRequestSize);
                sendResponse(null);
            } else {
                LogUtil.DEFAULT_LOG.warn("client subsciber's relations delete fail.");
            }
        } catch (Throwable t) {
            LogUtil.DEFAULT_LOG.error("long polling error:" + t.getMessage(), t.getCause());
        }
    }
}
