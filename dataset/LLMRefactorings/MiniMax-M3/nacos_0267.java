public class nacos_0267 {

    @Override
    public void addConfigInfoGrayAtomic(long configGrayId, ConfigInfo configInfo, String grayName,
        String grayRule,
        String srcIp, String srcUser) {
        String appNameTmp = StringUtils.defaultEmptyIfBlank(configInfo.getAppName());
        String tenantTmp = StringUtils.defaultEmptyIfBlank(configInfo.getTenant());
        String grayNameTmp = normalizeGrayField(grayName);
        String grayRuleTmp = normalizeGrayField(grayRule);
        String md5 = MD5Utils.md5Hex(configInfo.getContent(), Constants.ENCODE);
        
        ConfigInfoGrayMapper configInfoGrayMapper =
            mapperManager.findMapper(dataSourceService.getDataSourceType(),
                TableConstant.CONFIG_INFO_GRAY);
        
        final String sql = configInfoGrayMapper.insert(
            Arrays.asList("id", "data_id", "group_id", "tenant_id", "gray_name", "gray_rule",
                "app_name", "content",
                "md5", "src_ip", "src_user", "gmt_create", "gmt_modified"));
        
        Timestamp time = new Timestamp(System.currentTimeMillis());
        final Object[] args =
            new Object[] {configGrayId, configInfo.getDataId(), configInfo.getGroup(), tenantTmp,
                grayNameTmp, grayRuleTmp, appNameTmp, configInfo.getContent(), md5, srcIp,
                srcUser, time, time};
        EmbeddedStorageContextHolder.addSqlContext(sql, args);
    }
    
    private static String normalizeGrayField(String value) {
        return StringUtils.isBlank(value) ? StringUtils.EMPTY : value.trim();
    }
}
