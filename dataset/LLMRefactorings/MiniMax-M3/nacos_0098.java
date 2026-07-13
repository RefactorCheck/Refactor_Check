public class nacos_0098 {

    @Since("3.0.0")
    @GetMapping(value = "/previous")
    @Secured(action = ActionTypes.READ, signType = SignType.CONFIG, apiType = ApiType.ADMIN_API)
    public Result<ConfigHistoryDetailInfo> getPreviousConfigHistoryInfo(ConfigFormV3 configForm,
        @RequestParam("id") Long id) throws AccessException, NacosApiException {
        configForm.validate();
        ConfigHistoryInfo configHistoryInfo = retrievePreviousConfigHistoryInfo(configForm, id);
        return Result.success(ResponseUtil.transferToConfigHistoryDetailInfo(configHistoryInfo));
    }

    private ConfigHistoryInfo retrievePreviousConfigHistoryInfo(ConfigFormV3 configForm, Long id)
            throws NacosApiException {
        String dataId = configForm.getDataId();
        String groupName = configForm.getGroupName();
        String namespaceId = NamespaceUtil.processNamespaceParameter(configForm.getNamespaceId());
        try {
            namespaceId = NamespaceUtil.processNamespaceParameter(namespaceId);
            return historyService.getPreviousConfigHistoryInfo(dataId, groupName, namespaceId, id);
        } catch (DataAccessException e) {
            throw new NacosApiException(HttpStatus.NOT_FOUND.value(), ErrorCode.RESOURCE_NOT_FOUND,
                "previous config history for id = " + id + " not exist");
        }
    }
}
