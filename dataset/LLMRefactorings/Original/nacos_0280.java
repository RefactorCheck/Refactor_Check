public class nacos_0280 {

            @Override
            public void run() {
                if (cancel.get()) {
                    return;
                }
                try {
                    String currentMd5 = md5Cache.get(cacheKey);
                    AgentSpecQueryResponse response =
                        aiClientProxy.queryAgentSpec(agentSpecName, null, null, currentMd5);
                    processAgentSpec(agentSpecName, cacheKey, response);
                } catch (NacosException e) {
                    if (e.getErrCode() == NacosException.NOT_FOUND) {
                        processAgentSpec(agentSpecName, cacheKey, null);
                    } else if (e.getErrCode() == NacosException.NOT_MODIFIED) {
                        // No content change, keep local cache and skip callback.
                    } else {
                        LOGGER.warn(
                            "AgentSpec updater query failed: name={}, err={}",
                            agentSpecName, e.getErrMsg());
                    }
                } finally {
                    if (!cancel.get()) {
                        updaterExecutor.schedule(this, updateIntervalMillis,
                            TimeUnit.MILLISECONDS);
                    }
                }
            }
}
