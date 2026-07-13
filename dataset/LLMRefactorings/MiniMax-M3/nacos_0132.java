public class nacos_0132 {

    @Since("3.0.0")
    @GetMapping("/searchDetail")
    @Secured(action = ActionTypes.READ, signType = SignType.CONFIG, apiType = ApiType.CONSOLE_API)
    @ExtractorManager.Extractor(httpExtractor = ConfigBlurSearchHttpParamExtractor.class)
    public Result<Page<ConfigBasicInfo>> getConfigListByContent(ConfigFormV3 configForm,
        PageForm pageForm,
        String configDetail, @RequestParam(defaultValue = "blur") String search)
        throws NacosException {
        configForm.blurSearchValidate();
        pageForm.validate();
        Map<String, Object> configAdvanceInfo = buildConfigAdvanceInfo(configForm, configDetail);
        int pageNo = pageForm.getPageNo();
        int pageSize = pageForm.getPageSize();
        String namespaceId = NamespaceUtil.processNamespaceParameter(configForm.getNamespaceId());
        String dataId = configForm.getDataId();
        String groupName = configForm.getGroupName();
        
        return Result.success(
            configProxy.getConfigListByContent(search, pageNo, pageSize, dataId, groupName,
                namespaceId,
                configAdvanceInfo));
    }
    
    private Map<String, Object> buildConfigAdvanceInfo(ConfigFormV3 configForm, String configDetail) {
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
        return configAdvanceInfo;
    }
}
