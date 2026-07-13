public class nacos_0103 {

    private void refreshContentAndCheck(RpcClient rpcClient, CacheData cacheData,
        boolean notify) {
        try {
            ConfigResponse response =
                this.queryConfigInner(rpcClient, cacheData.dataId, cacheData.group,
                    cacheData.tenant, requestTimeout, notify);
            updateCacheData(cacheData, response);
            if (notify) {
                logDataReceived(cacheData, response);
            }
            cacheData.checkListenerMd5();
        } catch (Exception e) {
            LOGGER.error("refresh content and check md5 fail ,dataId={},group={},tenant={} ",
                cacheData.dataId,
                cacheData.group, cacheData.tenant, e);
        }
    }

    private void updateCacheData(CacheData cacheData, ConfigResponse response) {
        cacheData.setEncryptedDataKey(response.getEncryptedDataKey());
        cacheData.setContent(response.getContent());
        if (null != response.getConfigType()) {
            cacheData.setType(response.getConfigType());
        }
    }

    private void logDataReceived(CacheData cacheData, ConfigResponse response) {
        LOGGER.info(
            "[{}] [data-received] dataId={}, group={}, tenant={}, md5={}, type={}",
            agent.getName(),
            cacheData.dataId, cacheData.group, cacheData.tenant, cacheData.getMd5(),
            response.getConfigType());
    }
}
