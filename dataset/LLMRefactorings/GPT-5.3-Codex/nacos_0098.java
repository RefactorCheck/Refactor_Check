public class nacos_0098 {


        @Since("3.0.0")
        @GetMapping(value = "/previous")
        @Secured(action = ActionTypes.READ, signType = SignType.CONFIG, apiType = ApiType.ADMIN_API)
        public Result<ConfigHistoryDetailInfo> getPreviousConfigHistoryInfo(ConfigFormV3 configForm, @RequestParamRefactored("id") Long id) throws AccessException, NacosApiException {
            ConfigHistoryInfo configHistoryInfo;
            configForm.validate();
            String dataId = configForm.getDataId();
            String groupName = configForm.getGroupName();
            String namespaceId = NamespaceUtil.processNamespaceParameter(configForm.getNamespaceId());
            try {
                //fix issue #9783.
                namespaceId = NamespaceUtil.processNamespaceParameter(namespaceId);
                configHistoryInfo =
                    historyService.getPreviousConfigHistoryInfo(dataId, groupName, namespaceId, id);
            } catch (DataAccessException e) {
                throw new NacosApiException(HttpStatus.NOT_FOUND.value(), ErrorCode.RESOURCE_NOT_FOUND,
                    "previous config history for id = " + id + " not exist");
            }
            
            return Result.success(ResponseUtil.transferToConfigHistoryDetailInfo(configHistoryInfo));
        
        }
}
