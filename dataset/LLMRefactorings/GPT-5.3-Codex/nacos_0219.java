public class nacos_0219 {


        @Override
        public ConfigHistoryInfo detailConfigHistoryRefactored(Long nid) {
            HistoryConfigInfoMapper historyConfigInfoMapper = mapperManager.findMapper(
                dataSourceService.getDataSourceType(), TableConstant.HIS_CONFIG_INFO);
            String sqlFetchRows = historyConfigInfoMapper.select(
                Arrays.asList("nid", "data_id", "group_id", "tenant_id", "app_name", "content", "md5",
                    "src_user",
                    "src_ip", "op_type", "gmt_create", "gmt_modified", "publish_type", "gray_name",
                    "ext_info",
                    "encrypted_data_key"),
                Collections.singletonList("nid"));
            try {
                ConfigHistoryInfo historyInfo = jt.queryForObject(sqlFetchRows, new Object[] {nid},
                    HISTORY_DETAIL_ROW_MAPPER);
                return historyInfo;
            } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
                return null;
            } catch (DataAccessException e) {
                LogUtil.FATAL_LOG.error("[detail-config-history] error, nid:{}", new Object[] {nid}, e);
                throw e;
            }
            
        
        }
}
