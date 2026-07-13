public class nacos_0282 {

        @Override
        public ConfigQueryChainResponse handle(ConfigQueryChainRequest request) throws IOException {
            ConfigQueryChainResponse response = new ConfigQueryChainResponse();
            
            String dataId = request.getDataId();
            String group = request.getGroup();
            String tenant = request.getTenant();
            
            CacheItem cacheItem = ConfigChainEntryHandler.getThreadLocalCacheItem();
            String md5 = cacheItem.getConfigCache().getMd5();
            String content = ConfigDiskServiceFactory.getInstance().getContent(dataId, group, tenant);
            if (StringUtils.isBlank(content)) {
                response.setStatus(ConfigQueryChainResponse.ConfigQueryStatus.CONFIG_NOT_FOUND);
                return response;
            }
            long lastModified = cacheItem.getConfigCache().getLastModifiedTs();
            String encryptedDataKey = cacheItem.getConfigCache().getEncryptedDataKey();
            String configType = cacheItem.getType();
            response.setContent(content);
            response.setMd5(md5);
            response.setLastModified(lastModified);
            response.setEncryptedDataKey(encryptedDataKey);
            response.setConfigType(configType);
            response.setStatus(ConfigQueryChainResponse.ConfigQueryStatus.CONFIG_FOUND_FORMAL);
            
            return response;
        }
}
