public class nacos_0085 {


        @Since("3.1.0")
        @PutMapping("/metadata")
        @Secured(action = ActionTypes.WRITE, signType = SignType.CONFIG, apiType = ApiType.ADMIN_API)
        public Result<Boolean> publishConfigMetadataRefactored(HttpServletRequest request, ConfigFormV3 configForm) throws NacosException {
            configForm.validate();
            String remoteIp = getRemoteIp(request);
            String configTags = configForm.getConfigTags();
            String description = configForm.getDesc();
            String dataId = configForm.getDataId();
            String group = configForm.getGroup();
            String namespaceId = NamespaceUtil.processNamespaceParameter(configForm.getNamespaceId());
            configInfoPersistService.updateConfigInfoMetadata(dataId, group, namespaceId, configTags,
                description);
            final Timestamp time = TimeUtils.getCurrentTime();
            ConfigTraceService.logPersistenceEvent(dataId, group, namespaceId, null, time.getTime(),
                remoteIp,
                ConfigTraceService.PERSISTENCE_EVENT_METADATA, ConfigTraceService.PERSISTENCE_TYPE_PUB,
                null);
            ConfigChangePublisher.notifyConfigChange(
                new ConfigDataChangeEvent(dataId, group, namespaceId, time.getTime()));
            return Result.success(true);
        
        }
}
