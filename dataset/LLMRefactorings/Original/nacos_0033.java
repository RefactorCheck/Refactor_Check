public class nacos_0033 {

        @Override
        public Page<ConfigHistoryInfo> findConfigHistory(String dataId, String group, String tenant,
            int pageNo,
            int pageSize) {
            PaginationHelper<ConfigHistoryInfo> helper = createPaginationHelper();
            String tenantTmp = StringUtils.isBlank(tenant) ? StringUtils.EMPTY : tenant;
            
            MapperContext context = new MapperContext((pageNo - 1) * pageSize, pageSize);
            context.putWhereParameter(FieldConstant.DATA_ID, dataId);
            context.putWhereParameter(FieldConstant.GROUP_ID, group);
            context.putWhereParameter(FieldConstant.TENANT_ID, tenantTmp);
            
            HistoryConfigInfoMapper historyConfigInfoMapper = mapperManager.findMapper(
                dataSourceService.getDataSourceType(), TableConstant.HIS_CONFIG_INFO);
            
            String sqlCountRows =
                historyConfigInfoMapper.count(Arrays.asList("data_id", "group_id", "tenant_id"));
            MapperResult sqlFetchRows = historyConfigInfoMapper.pageFindConfigHistoryFetchRows(context);
            
            Page<ConfigHistoryInfo> page;
            try {
                page = helper.fetchPage(sqlCountRows, sqlFetchRows.getSql(),
                    sqlFetchRows.getParamList().toArray(), pageNo,
                    pageSize, HISTORY_LIST_ROW_MAPPER);
            } catch (DataAccessException e) {
                LogUtil.FATAL_LOG.error("[list-config-history] error, dataId:{}, group:{}",
                    new Object[] {dataId, group},
                    e);
                throw e;
            }
            return page;
        }
}
