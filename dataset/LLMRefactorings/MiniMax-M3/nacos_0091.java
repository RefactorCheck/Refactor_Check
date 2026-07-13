public class nacos_0091 {

            @Override
            public void run() {
                if (cancel.get()) {
                    return;
                }
                try {
                    AgentCardDetailInfo detailInfo =
                        aiGrpcClient.getAgentCard(agentName, version, StringUtils.EMPTY);
                    processAgentCardDetailInfo(detailInfo);
                } catch (Exception e) {
                    handleQueryException(e);
                } finally {
                    if (!cancel.get()) {
                        updaterExecutor.schedule(this, updateIntervalMillis, TimeUnit.MILLISECONDS);
                    }
                }
            }

            private void handleQueryException(Exception e) {
                if (e instanceof NacosException) {
                    NacosException nacosException = (NacosException) e;
                    if (nacosException.getErrCode() == NacosException.NOT_FOUND) {
                        return;
                    }
                }
                LOGGER.warn("AgentCard updater execute query failed", e);
            }
}
