public class nacos_0261 {

    public void checkLocalConfig(CacheData cacheData) {
        final String dataId = cacheData.dataId;
        final String group = cacheData.group;
        final String tenant = cacheData.tenant;
        final String envName = cacheData.envName;
        
        File file = LocalConfigInfoProcessor.getFailoverFile(envName, dataId, group, tenant);
        
        if (!cacheData.isUseLocalConfigInfo() && file.exists()) {
            String content = loadFailoverAndUpdateCacheData(cacheData, file, envName, dataId, group, tenant);
            final String md5 = MD5Utils.md5Hex(content, Constants.ENCODE);
            LOGGER.warn(
                "[{}] [failover-change] failover file created. dataId={}, group={}, tenant={}, md5={}",
                envName, dataId, group, tenant, md5);
            return;
        }
        
        if (cacheData.isUseLocalConfigInfo() && !file.exists()) {
            cacheData.setUseLocalConfigInfo(false);
            LOGGER.warn(
                "[{}] [failover-change] failover file deleted. dataId={}, group={}, tenant={}",
                envName,
                dataId, group, tenant);
            return;
        }
        
        if (cacheData.isUseLocalConfigInfo() && file.exists()
            && cacheData.getLocalConfigInfoVersion() != file.lastModified()) {
            String content = loadFailoverAndUpdateCacheData(cacheData, file, envName, dataId, group, tenant);
            final String md5 = MD5Utils.md5Hex(content, Constants.ENCODE);
            LOGGER.warn(
                "[{}] [failover-change] failover file changed. dataId={}, group={}, tenant={}, md5={}",
                envName, dataId, group, tenant, md5);
        }
    }
    
    private String loadFailoverAndUpdateCacheData(CacheData cacheData, File file, String envName, 
            String dataId, String group, String tenant) {
        String content = LocalConfigInfoProcessor.getFailover(envName, dataId, group, tenant);
        cacheData.setUseLocalConfigInfo(true);
        cacheData.setLocalConfigInfoVersion(file.lastModified());
        cacheData.setContent(content);
        return content;
    }
}
