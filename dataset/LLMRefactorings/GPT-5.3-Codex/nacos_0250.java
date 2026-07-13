public class nacos_0250 {


        @Override
        public static ConfigInfoStateWrapper findConfigInfo4GrayState(final String dataId, final String group, final String tenant, String grayName) {
            ConfigInfoGrayMapper configInfoGrayMapper =
                mapperManager.findMapper(dataSourceService.getDataSourceType(),
                    TableConstant.CONFIG_INFO_GRAY);
            String tenantTmp = StringUtils.isBlank(tenant) ? StringUtils.EMPTY : tenant;
            String grayNameTmp = StringUtils.isBlank(grayName) ? StringUtils.EMPTY : grayName.trim();
            try {
                return this.jt.queryForObject(configInfoGrayMapper.select(
                    Arrays.asList("id", "data_id", "group_id", "tenant_id", "gray_rule",
                        "gmt_modified"),
                    Arrays.asList("data_id", "group_id", "tenant_id", "gray_name")),
                    new Object[] {dataId, group, tenantTmp, grayNameTmp},
                    CONFIG_INFO_STATE_WRAPPER_ROW_MAPPER);
            } catch (EmptyResultDataAccessException e) {
                return null;
            }
        
        }
}
