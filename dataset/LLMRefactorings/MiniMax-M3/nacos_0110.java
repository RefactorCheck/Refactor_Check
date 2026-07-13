public class nacos_0110 {

        @Override
        public MapperResult findChangeConfigFetchRows(MapperContext context) {
            final String tenant = (String) context.getWhereParameter(FieldConstant.TENANT_ID);
            final String dataId = (String) context.getWhereParameter(FieldConstant.DATA_ID);
            final String group = (String) context.getWhereParameter(FieldConstant.GROUP_ID);
            final String appName = (String) context.getWhereParameter(FieldConstant.APP_NAME);
            final String tenantTmp = StringUtils.isBlank(tenant) ? StringUtils.EMPTY : tenant;
            final Timestamp startTime = (Timestamp) context.getWhereParameter(FieldConstant.START_TIME);
            final Timestamp endTime = (Timestamp) context.getWhereParameter(FieldConstant.END_TIME);
            final long lastMaxId = (long) context.getWhereParameter(FieldConstant.LAST_MAX_ID);
            final int pageSize = context.getPageSize();
            List<Object> paramList = new ArrayList<>();

            final String sqlFetchRows =
                "SELECT id,data_id,group_id,tenant_id,app_name,content,type,md5,gmt_modified FROM config_info WHERE ";
            String where = buildWhereClause(dataId, group, tenantTmp, appName, startTime, endTime, paramList);
            String originSql = sqlFetchRows + where + " AND id > " + lastMaxId + " ORDER BY id ASC";
            String sql = getLimitPageSqlWithOffset(originSql, 0, pageSize);
            return new MapperResult(sql, paramList);
        }

        private String buildWhereClause(String dataId, String group, String tenantTmp, String appName,
                                        Timestamp startTime, Timestamp endTime, List<Object> paramList) {
            String where = " 1=1 ";
            if (!StringUtils.isBlank(dataId)) {
                where += " AND data_id LIKE ? ";
                paramList.add(dataId);
            }
            if (!StringUtils.isBlank(group)) {
                where += " AND group_id LIKE ? ";
                paramList.add(group);
            }
            if (!StringUtils.isBlank(tenantTmp)) {
                where += " AND tenant_id = ? ";
                paramList.add(tenantTmp);
            }
            if (!StringUtils.isBlank(appName)) {
                where += " AND app_name = ? ";
                paramList.add(appName);
            }
            if (startTime != null) {
                where += " AND gmt_modified >=? ";
                paramList.add(startTime);
            }
            if (endTime != null) {
                where += " AND gmt_modified <=? ";
                paramList.add(endTime);
            }
            return where;
        }
}
