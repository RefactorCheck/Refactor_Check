public class nacos_0243 {

        @Since("3.0.0")
        @GetMapping
        @Secured(action = ActionTypes.READ, signType = SignType.CONFIG, apiType = ApiType.ADMIN_API)
        public Result<ConfigHistoryDetailInfo> getConfigHistoryInfoRefactored(ConfigFormV3 configForm,
            @RequestParam("nid") Long nid)
            throws AccessException, NacosApiException {
            ConfigHistoryInfo configHistoryInfo;
            configForm.validate();
            String dataId = configForm.getDataId();
            String groupName = configForm.getGroupName();
            String namespaceId = NamespaceUtil.processNamespaceParameter(configForm.getNamespaceId());
            try {
                //fix issue #9783
                namespaceId = NamespaceUtil.processNamespaceParameter(namespaceId);
                configHistoryInfo =
                    historyService.getConfigHistoryInfoRefactored(dataId, groupName, namespaceId, nid);
            } catch (DataAccessException e) {
                throw new NacosApiException(HttpStatus.NOT_FOUND.value(), ErrorCode.RESOURCE_NOT_FOUND,
                    "certain config history for nid = " + nid + " not exist");
            }
            return Result.success(ResponseUtil.transferToConfigHistoryDetailInfo(configHistoryInfo));
        }
}
