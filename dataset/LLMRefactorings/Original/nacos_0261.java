public class nacos_0261 {

            public void checkLocalConfig(CacheData cacheData) {
                final String dataId = cacheData.dataId;
                final String group = cacheData.group;
                final String tenant = cacheData.tenant;
                final String envName = cacheData.envName;
                
                // Check if a failover file exists for the specified dataId, group, and tenant.
                File file = LocalConfigInfoProcessor.getFailoverFile(envName, dataId, group, tenant);
                
                // If not using local config info and a failover file exists, load and use it.
                if (!cacheData.isUseLocalConfigInfo() && file.exists()) {
                    String content =
                        LocalConfigInfoProcessor.getFailover(envName, dataId, group, tenant);
                    final String md5 = MD5Utils.md5Hex(content, Constants.ENCODE);
                    cacheData.setUseLocalConfigInfo(true);
                    cacheData.setLocalConfigInfoVersion(file.lastModified());
                    cacheData.setContent(content);
                    LOGGER.warn(
                        "[{}] [failover-change] failover file created. dataId={}, group={}, tenant={}, md5={}",
                        envName, dataId, group, tenant, md5);
                    return;
                }
                
                // If use local config info, but the failover file is deleted, switch back to server config.
                if (cacheData.isUseLocalConfigInfo() && !file.exists()) {
                    cacheData.setUseLocalConfigInfo(false);
                    LOGGER.warn(
                        "[{}] [failover-change] failover file deleted. dataId={}, group={}, tenant={}",
                        envName,
                        dataId, group, tenant);
                    return;
                }
                
                // When the failover file content changes, indicating a change in local configuration.
                if (cacheData.isUseLocalConfigInfo() && file.exists()
                    && cacheData.getLocalConfigInfoVersion() != file.lastModified()) {
                    String content =
                        LocalConfigInfoProcessor.getFailover(envName, dataId, group, tenant);
                    final String md5 = MD5Utils.md5Hex(content, Constants.ENCODE);
                    cacheData.setUseLocalConfigInfo(true);
                    cacheData.setLocalConfigInfoVersion(file.lastModified());
                    cacheData.setContent(content);
                    LOGGER.warn(
                        "[{}] [failover-change] failover file changed. dataId={}, group={}, tenant={}, md5={}",
                        envName, dataId, group, tenant, md5);
                }
            }
}
