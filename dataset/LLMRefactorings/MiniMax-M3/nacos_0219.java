public class nacos_0219 {

        @Override
        public ConfigHistoryInfo detailConfigHistory(Long nid) {
            String sqlFetchRows = buildHistoryDetailSql();
            try {
                return jt.queryForObject(sqlFetchRows, new Object[] {nid}, HISTORY_DETAIL_ROW_MAPPER);
            } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
                return null;
            } catch (DataAccessException e) {
                LogUtil.FATAL_LOG.error("[detail-config-history] error, nid:{}", new Object[] {nid}, e);
                throw e;
            }
        }

        private String buildHistoryDetailSql() {
            HistoryConfigInfoMapper historyConfigInfoMapper = mapperManager.findMapper(
                dataSourceService.getDataSourceType(), TableConstant.HIS_CONFIG_INFO);
            return historyConfigInfoMapper.select(
                Arrays.asList("nid", "data_id", "group_id", "tenant_id", "app_name", "content", "md5",
                    "src_user",
                    "src_ip", "op_type", "gmt_create", "gmt_modified", "publish_type", "gray_name",
                    "ext_info",
                    "encrypted_data_key"),
                Collections.singletonList("nid"));
        }
}
