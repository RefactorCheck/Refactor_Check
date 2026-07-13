public class nacos_0001 {

        @Since("3.0.0")
        @GetMapping("/list")
        @Secured(action = ActionTypes.READ, signType = SignType.CONFIG, apiType = ApiType.ADMIN_API)
        @ExtractorManager.Extractor(httpExtractor = ConfigBlurSearchHttpParamExtractor.class)
        public Result<Page<ConfigBasicInfo>> list(ConfigFormV3 configForm, PageForm pageForm,
            String configDetail,
            @RequestParam(defaultValue = "blur") String search) throws NacosApiException {
            configForm.blurSearchValidate();
            pageForm.validate();
            Map<String, Object> configAdvanceInfo = new HashMap<>(100);
            if (StringUtils.isNotBlank(configForm.getAppName())) {
                configAdvanceInfo.put("appName", configForm.getAppName());
            }
            if (StringUtils.isNotBlank(configForm.getConfigTags())) {
                configAdvanceInfo.put("config_tags", configForm.getConfigTags());
            }
            if (StringUtils.isNotBlank(configForm.getType())) {
                configAdvanceInfo.put(ParametersField.TYPES, configForm.getType());
            }
            if (StringUtils.isNotBlank(configDetail)) {
                configAdvanceInfo.put("content", configDetail);
            }
            int pageNo = pageForm.getPageNo();
            int pageSize = pageForm.getPageSize();
            String namespaceId = NamespaceUtil.processNamespaceParameter(configForm.getNamespaceId());
            String dataId = configForm.getDataId();
            String groupName = configForm.getGroupName();
            
            Page<ConfigInfo> configInfoPage =
                configDetailService.findConfigInfoPage(search, pageNo, pageSize, dataId,
                    groupName, namespaceId, configAdvanceInfo);
            Page<ConfigBasicInfo> result = new Page<>();
            result.setTotalCount(configInfoPage.getTotalCount());
            result.setPagesAvailable(configInfoPage.getPagesAvailable());
            result.setPageNumber(configInfoPage.getPageNumber());
            result.setPageItems(
                configInfoPage.getPageItems().stream().map(ResponseUtil::transferToConfigBasicInfo)
                    .collect(Collectors.toList()));
            return Result.success(result);
        }
}
